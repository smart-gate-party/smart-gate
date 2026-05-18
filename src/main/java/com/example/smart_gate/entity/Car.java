package com.example.smart_gate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "allowed_cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plateNumber; // Номер авто (напр. "A123BB")

    private String ownerName;   // ФИО владельца

    private boolean isActive;   // Разрешен ли въезд (активен ли пропуск)

    private LocalDateTime createdAt; // Когда добавили в базу

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}