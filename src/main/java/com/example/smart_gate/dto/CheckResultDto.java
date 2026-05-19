package com.example.smart_gate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckResultDto {
    private boolean allowed;
    private String plateNumber;
    private String ownerName;
    private String message;
}