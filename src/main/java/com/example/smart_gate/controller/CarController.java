package com.example.smart_gate.controller;

import com.example.smart_gate.dto.CarDto;
import com.example.smart_gate.entity.Car;
import com.example.smart_gate.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<Car>> getAll() {
        return ResponseEntity.ok(carService.getAll());
    }

    @PostMapping
    public ResponseEntity<Car> add(@Valid @RequestBody CarDto dto) {
        return ResponseEntity.ok(carService.add(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id,
                                      @Valid @RequestBody CarDto dto) {
        return ResponseEntity.ok(carService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}