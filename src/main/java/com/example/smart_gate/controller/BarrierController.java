package com.example.smart_gate.controller;

import com.example.smart_gate.dto.CheckResultDto;
import com.example.smart_gate.dto.PlateRequestDto;
import com.example.smart_gate.service.BarrierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barrier")
@RequiredArgsConstructor
public class BarrierController {

    private final BarrierService barrierService;

    @PostMapping("/check")
    public ResponseEntity<CheckResultDto> checkPlate(@Valid @RequestBody PlateRequestDto request) {
        return ResponseEntity.ok(barrierService.checkPlate(request));
    }

    @PostMapping("/open")
    public ResponseEntity<String> open() {
        return ResponseEntity.ok("Шлагбаум открыт");
    }

    @PostMapping("/close")
    public ResponseEntity<String> close() {
        return ResponseEntity.ok("Шлагбаум закрыт");
    }
}