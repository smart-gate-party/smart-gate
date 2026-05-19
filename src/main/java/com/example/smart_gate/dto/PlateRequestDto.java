package com.example.smart_gate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlateRequestDto {
    @NotBlank
    private String plate;
}