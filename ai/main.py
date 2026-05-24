import cv2
import easyocr
import re
import serial
import time
import requests
import threading
import concurrent.futures
from flask import Flask

# =====================================================================
# 1. НАСТРОЙКИ
# =====================================================================
reader = easyocr.Reader(['en'], gpu=False)
ALLOW_CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
PLATE_PATTERN = re.compile(r'^[A-Z0-9]{4,9}$')

plate_buffer = {}
REQUIRED_FRAMES = 2
last_printed_plate = ""
frame_count = 0

SPRING_URL = 'https://smart-gate-production.up.railway.app/api/barrier/check'

ARDUINO_PORT = 'COM3'

try:
    arduino = serial.Serial(port=ARDUINO_PORT, baudrate=9600, timeout=1)
    time.sleep(2)
    print(f"✅ Arduino подключен на порту {ARDUINO_PORT}")
except Exception as e:
    arduino = None
    print(f"⚠️ Arduino не найден. Режим симуляции.")

# =====================================================================
# 2. FLASK СЕРВЕР ДЛЯ РУЧНОГО УПРАВЛЕНИЯ
# =====================================================================
flask_app = Flask(__name__)

@flask_app.route('/open', methods=['POST'])
def open_barrier():
    if arduino:
        arduino.write(b'M')
        print("🚀 Ручное открытие через сайт!")
    else:
        print("🤖 (Симуляция) Ручное открытие")
    return 'OK'

@flask_app.route('/close', methods=['POST'])
def close_barrier():
    if arduino:
        arduino.write(b'C')
        print("🔴 Ручное закрытие через сайт")
    else:
        print("🤖 (Симуляция) Закрытие")
    return 'OK'

threading.Thread(
    target=lambda: flask_app.run(host='0.0.0.0', port=5001, debug=False, use_reloader=False),
    daemon=True
).start()
print("✅ Flask сервер запущен на порту 5001")

# =====================================================================
# 3. СБРОС НОМЕРА ПОСЛЕ ПРОЕЗДА
# =====================================================================
def reset_last_plate(plate):
    global last_printed_plate
    if last_printed_plate == plate:
        last_printed_plate = ""
        print(f"🔄 Номер {plate} сброшен — можно снова проезжать")

# =====================================================================
# 4. ИСПРАВЛЕНИЕ ТИПИЧНЫХ ОШИБОК OCR
# =====================================================================
def fix_plate(text):
    fixes = {
        'О': 'O', 'А': 'A', 'В': 'B', 'С': 'C',
        'Е': 'E', 'К': 'K', 'М': 'M', 'Н': 'H',
        'Р': 'P', 'Т': 'T', 'Х': 'X', 'У': 'Y',
    }
    result = ""
    for char in text.upper():
        result += fixes.get(char, char)
    return result

# =====================================================================
# 5. ПРОВЕРКА НОМЕРА ЧЕРЕЗ SPRING API
# =====================================================================
def check_plate_and_trigger_gate(plate_text):
    print(f"📡 Проверяем номер [{plate_text}]...")

    try:
        response = requests.post(
            SPRING_URL,
            json={'plate': plate_text},
            timeout=5
        )

        result = response.json()

        if result.get('allowed'):
            owner = result.get('ownerName', 'Неизвестно')
            print(f"🟢 [ДОСТУП РАЗРЕШЕН] Владелец: {owner}")

            if arduino:
                arduino.write(b'O')
                print("🚀 Сигнал на Arduino отправлен!")
            else:
                print("🤖 (Симуляция) Шлагбаум открыт")

            threading.Timer(15.0, lambda: reset_last_plate(plate_text)).start()
        else:
            print(f"🔴 [ДОСТУП ЗАПРЕЩЕН] Номер: {plate_text}")

    except requests.exceptions.ConnectionError:
        print("⚠️ Spring недоступен.")
    except Exception as e:
        print(f"⚠️ Ошибка: {e}")

# =====================================================================
# 6. ОБРАБОТКА КАДРА
# =====================================================================
def process_frame(frame):
    global plate_buffer, last_printed_plate
    current_frame_plates = []

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Улучшаем контраст
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
    gray = clahe.apply(gray)

    blurred = cv2.GaussianBlur(gray, (3, 3), 0)
    _, binary = cv2.threshold(blurred, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

    detection = reader.readtext(
        binary,
        allowlist=ALLOW_CHARS,
        batch_size=1,
        workers=0,
        decoder='greedy'
    )

    for (bbox, text, prob) in detection:
        clean_text = fix_plate(text.upper().replace(" ", "").replace("-", "").strip())

        if prob < 0.40 or len(clean_text) < 4 or len(clean_text) > 9:
            continue

        if not PLATE_PATTERN.match(clean_text):
            continue

        current_frame_plates.append(clean_text)

        tl = (int(bbox[0][0]), int(bbox[0][1]))
        br = (int(bbox[2][0]), int(bbox[2][1]))
        cv2.rectangle(frame, tl, br, (0, 255, 0), 2)
        cv2.putText(frame, clean_text, (tl[0], tl[1] - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)

    for plate in current_frame_plates:
        plate_buffer[plate] = plate_buffer.get(plate, 0) + 1
        if plate_buffer[plate] >= REQUIRED_FRAMES and plate != last_printed_plate:
            threading.Thread(target=check_plate_and_trigger_gate, args=(plate,)).start()
            last_printed_plate = plate
            plate_buffer[plate] = 0

    for plate in list(plate_buffer.keys()):
        if plate not in current_frame_plates:
            del plate_buffer[plate]

    return frame

# =====================================================================
# 7. ЗАПУСК
# =====================================================================
cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

executor = concurrent.futures.ThreadPoolExecutor(max_workers=1)
ocr_future = None
last_processed_frame = None

print("\n🚀 SmartGate AI запущен!")

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    frame_count += 1

    if frame_count % 15 == 0:
        if ocr_future is None or ocr_future.done():
            if ocr_future and ocr_future.done():
                last_processed_frame = ocr_future.result()
            ocr_future = executor.submit(process_frame, frame.copy())

    display_frame = last_processed_frame if last_processed_frame is not None else frame
    cv2.imshow('SmartGate AI', display_frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
if arduino:
    arduino.close()
cv2.destroyAllWindows()