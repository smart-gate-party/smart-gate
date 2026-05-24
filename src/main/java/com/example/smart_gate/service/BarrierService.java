package com.example.smart_gate.service;

import com.example.smart_gate.dto.CheckResultDto;
import com.example.smart_gate.dto.PlateRequestDto;
import com.example.smart_gate.entity.AccessLog;
import com.example.smart_gate.entity.Car;
import com.example.smart_gate.repository.AccessLogRepository;
import com.example.smart_gate.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarrierService {

    private final CarRepository carRepository;
    private final AccessLogRepository accessLogRepository;

    public CheckResultDto checkPlate(PlateRequestDto request) {
        String plate = request.getPlate().trim().toUpperCase();
        Optional<Car> carOpt = carRepository.findByPlateNumber(plate);

        boolean allowed = carOpt.isPresent() && carOpt.get().isActive();
        String owner = carOpt.map(Car::getOwnerName).orElse(null);

        accessLogRepository.save(AccessLog.builder()
                .plateNumber(plate)
                .accessGranted(allowed)
                .ownerName(owner)
                .build());

        return new CheckResultDto(
                allowed,
                plate,
                owner,
                allowed ? "Доступ разрешён" : "Доступ запрещён"
        );
    }

    public void manualOpen() {
        try {
            HttpURLConnection conn = (HttpURLConnection)
                    new URL("https://shading-unreeling-hurricane.ngrok-free.dev/open").openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getResponseCode();
        } catch (Exception e) {
            System.out.println("⚠️ Python недоступен: " + e.getMessage());
        }

        accessLogRepository.save(AccessLog.builder()
                .plateNumber("MANUAL")
                .accessGranted(true)
                .ownerName("Администратор")
                .build());
    }

    public void manualClose() {
        try {
            HttpURLConnection conn = (HttpURLConnection)
                    new URL("https://shading-unreeling-hurricane.ngrok-free.dev/close").openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getResponseCode();
        } catch (Exception e) {
            System.out.println("⚠️ Python недоступен: " + e.getMessage());
        }

        accessLogRepository.save(AccessLog.builder()
                .plateNumber("MANUAL")
                .accessGranted(false)
                .ownerName("Администратор")
                .build());
    }
}