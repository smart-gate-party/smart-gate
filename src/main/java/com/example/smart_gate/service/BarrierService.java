package com.example.smart_gate.service;

import com.example.smart_gate.dto.CheckResultDto;
import com.example.smart_gate.dto.PlateRequestDto;
import com.example.smart_gate.entity.AccessLog;
import com.example.smart_gate.entity.Car;
import com.example.smart_gate.repository.AccessLogRepository;
import com.example.smart_gate.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}