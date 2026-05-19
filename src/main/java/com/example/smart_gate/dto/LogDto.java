package com.example.smart_gate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogDto {
    private Long id;
    private String plateNumber;
    private String ownerName;
    private boolean accessGranted;
    private LocalDateTime timestamp;
}