package com.example.smart_gate.service;

import com.example.smart_gate.entity.AccessLog;
import com.example.smart_gate.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final AccessLogRepository accessLogRepository;

    public List<AccessLog> getAll() {
        return accessLogRepository.findAll();
    }

    public List<AccessLog> getLatest() {
        return accessLogRepository.findTop20ByOrderByTimestampDesc();
    }
}