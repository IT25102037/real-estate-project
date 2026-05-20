package com.realestate.project.dto;

import lombok.Builder;
import lombok.Data;
import com.realestate.project.enums.PaymentStatus;
import com.realestate.project.enums.PaymentType;

import java.math.BigDecimal;

@Data
@Builder

public class PaymentResponse {

    private Long paymentId;

    private BigDecimal amount;

    private String transactionId;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private Long propertyId;

    private Long bankAccountId;
}
