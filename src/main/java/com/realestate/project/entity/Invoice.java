package com.realestate.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    private BigDecimal totalAmount;

    private LocalDateTime issuedDate;

    private Long paymentId;

    @PrePersist
    public void prePersist() {
        issuedDate = LocalDateTime.now();
    }
}
