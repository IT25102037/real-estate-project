package com.realestate.project.dto;

import lombok.Builder;
import lombok.Data;
import com.realestate.project.enums.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RentPaymentResponse {

    private Long id;

    private Long userId;

    private Long propertyId;

    private String rentMonth;

    private BigDecimal amount;

    private RentStatus status;

    private LocalDate dueDate;

    private LocalDateTime paidDate;
}
