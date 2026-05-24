# SmartGate AI 🚗🚧

> AI-powered license plate recognition and automated gate control system for modern campuses and residential complexes.

🌐 **Live Demo:** [smart-gate-production.up.railway.app](https://smart-gate-production.up.railway.app)

---

## 📋 Overview

SmartGate AI is a smart barrier management system that uses computer vision to automatically recognize vehicle license plates and control gate access. Administrators can manage vehicles, monitor access history, and manually control the barrier through a web dashboard.

---

## ✨ Features

- 🎥 **Real-time license plate recognition** using camera + EasyOCR
- 🚗 **Vehicle whitelist management** — add, activate, deactivate vehicles
- 📊 **Access monitoring dashboard** — live logs and statistics
- 🔐 **JWT-secured admin panel** — protected routes and API endpoints
- 🎛️ **Manual barrier control** — open/close via web interface
- 📡 **Arduino integration** — physical servo motor control via USB
- ☁️ **Cloud deployment** — hosted on Railway with PostgreSQL

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 3.2.5 | REST API framework |
| Spring Security + JWT | Authentication & authorization |
| Spring Data JPA + Hibernate | Database ORM |
| PostgreSQL | Production database |
| Maven | Build tool |
| Docker | Containerization |

### AI / Computer Vision
| Technology | Purpose |
|------------|---------|
| Python 3.12 | AI service language |
| OpenCV | Camera capture & image processing |
| EasyOCR | License plate text recognition |
| Flask | Internal HTTP server for barrier control |
| PySerial | Arduino communication |

### Frontend
| Technology | Purpose |
|------------|---------|
| HTML5 / CSS3 / JavaScript | Web interface |
| Fetch API | REST API communication |
| localStorage | JWT token storage |

### Hardware
| Component | Purpose |
|-----------|---------|
| Arduino Uno | Microcontroller |
| SG90 Servo Motor | Physical barrier mechanism |

### DevOps
| Technology | Purpose |
|------------|---------|
| Railway | Cloud hosting & PostgreSQL |
| GitHub | Version control & CI/CD |
| Docker | Container deployment |
| ngrok | Local tunnel for hardware integration |

---

## 🏗️ Architecture

```
Camera (Laptop)
      ↓
Python AI Service (EasyOCR)
      ↓ HTTP POST /api/barrier/check
Spring Boot Backend (Railway)
      ↓ Query
PostgreSQL Database
      ↓ Response (allowed/denied)
Python AI Service
      ↓ Serial signal
Arduino Uno → SG90 Servo → Barrier
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Maven
- Python 3.12
- PostgreSQL
- Arduino IDE (for hardware)

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/smart-gate-party/smart-gate.git
cd smart-gate

# Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_gate
spring.datasource.username=postgres
spring.datasource.password=your_password

# Run the application
./mvnw spring-boot:run
```

### Python AI Setup

```bash
cd ai

# Install dependencies
pip install -r requirements.txt

# Run the AI service
python main.py
```

### Default Admin Credentials
```
Username: admin
Password: admin123
```

---

## 📁 Project Structure

```
smart-gate/
├── src/
│   └── main/
│       ├── java/com/example/smart_gate/
│       │   ├── controller/       # REST controllers
│       │   ├── service/          # Business logic
│       │   ├── repository/       # Data access layer
│       │   ├── entity/           # JPA entities
│       │   ├── dto/              # Data transfer objects
│       │   └── security/         # JWT & Spring Security
│       └── resources/
│           └── static/           # Frontend (HTML/CSS/JS)
├── ai/
│   ├── main.py                   # AI service + Flask server
│   └── requirements.txt
├── Dockerfile
└── pom.xml
```

---

## 🔌 API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Admin login | Public |
| POST | `/api/barrier/check` | Check plate (AI) | Public |
| POST | `/api/barrier/open` | Manual open | Admin |
| POST | `/api/barrier/close` | Manual close | Admin |
| GET | `/api/vehicles` | List vehicles | Admin |
| POST | `/api/vehicles` | Add vehicle | Admin |
| PUT | `/api/vehicles/{id}` | Update vehicle | Admin |
| DELETE | `/api/vehicles/{id}` | Delete vehicle | Admin |
| GET | `/api/logs` | Access history | Admin |
| GET | `/api/logs/latest` | Latest 20 logs | Admin |

---

## 👥 Team

| Name | Role |
|------|------|
| Bakytbekov Abdurazzak | Project Manager |
| Kydyev Mirlan | AI Engineer |
| Ametov Nurdin | DevSecOps Engineer |
| Turatov Asanbek | Backend Developer |
| Zhanarbekova Ainazik | Frontend Developer |

---

## 📄 License

This project was developed as part of a hackathon. All rights reserved © 2026 SmartGate AI Team.