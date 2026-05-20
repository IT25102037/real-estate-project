package com.realestate.project.entity;


import jakarta.persistence.*;
import lombok.*;
import com.realestate.project.enums.PaymentMethod;
import com.realestate.project.enums.PaymentStatus;
import com.realestate.project.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long propertyId;

    private Long bankAccountId;

    private BigDecimal amount;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    @PrePersist
    public void prePersist() {
        paymentDate = LocalDateTime.now();
    }
}
