package com.realestate.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paymentId;

    private String oldStatus;

    private String newStatus;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        updatedAt = LocalDateTime.now();
    }
}
