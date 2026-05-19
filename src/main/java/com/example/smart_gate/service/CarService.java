package com.example.smart_gate.service;

import com.example.smart_gate.dto.CarDto;
import com.example.smart_gate.entity.Car;
import com.example.smart_gate.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car add(CarDto dto) {
        Car car = Car.builder()
                .plateNumber(dto.getPlateNumber().trim().toUpperCase())
                .ownerName(dto.getOwnerName())
                .isActive(true)
                .build();
        return carRepository.save(car);
    }

    public Car update(Long id, CarDto dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Машина не найдена"));
        car.setPlateNumber(dto.getPlateNumber().trim().toUpperCase());
        car.setOwnerName(dto.getOwnerName());
        car.setActive(dto.isActive());
        return carRepository.save(car);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}