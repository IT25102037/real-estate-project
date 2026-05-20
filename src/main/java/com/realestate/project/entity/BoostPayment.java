package com.realestate.project.entity;

import jakarta.persistence.*;
import lombok.*;
import com.realestate.project.enums.BoostStatus;

import java.time.LocalDate;

@Entity
@Table(name = "boost_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoostPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long propertyId;

    private Long paymentId;

    private String boostPackage;

    private Integer boostDays;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BoostStatus status;
}
