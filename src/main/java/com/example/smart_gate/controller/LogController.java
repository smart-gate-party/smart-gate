package com.example.smart_gate.controller;

import com.example.smart_gate.entity.AccessLog;
import com.example.smart_gate.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<AccessLog>> getAll() {
        return ResponseEntity.ok(logService.getAll());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<AccessLog>> getLatest() {
        return ResponseEntity.ok(logService.getLatest());
    }
}