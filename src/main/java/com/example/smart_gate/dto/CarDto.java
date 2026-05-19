package com.example.smart_gate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarDto {
    @NotBlank
    private String plateNumber;
    private String ownerName;
    private boolean isActive;
}