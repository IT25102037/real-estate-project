package com.realestate.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import com.realestate.project.entity.PaymentStatus;
import com.realestate.project.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;

    @Min(value = 1, message = "Amount must be greater than 0")
    private double amount;

    private LocalDate date;

    private PaymentStatus status;

    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    @NotNull(message = "User is required")
    private Long userId;

    @NotNull(message = "Property is required")
    private Long propertyId;
}
