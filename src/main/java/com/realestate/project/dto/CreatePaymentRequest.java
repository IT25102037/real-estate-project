package com.realestate.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.realestate.project.enums.PaymentMethod;
import com.realestate.project.enums.PaymentType;

import java.math.BigDecimal;

@Data

public class CreatePaymentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Amount is required")

    @DecimalMin(
            value = "1.0",
            message = "Amount must be greater than 0"
    )

    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    private Long bankAccountId;
}
