package com.realestate.project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateRentPaymentRequest {

    private Long userId;

    private Long propertyId;

    private String rentMonth;

    private BigDecimal amount;

    private LocalDate dueDate;
}
