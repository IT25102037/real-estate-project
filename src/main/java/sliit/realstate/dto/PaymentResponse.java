package sliit.realstate.dto;

import lombok.Builder;
import lombok.Data;
import sliit.realstate.enums.PaymentStatus;
import sliit.realstate.enums.PaymentType;

import java.math.BigDecimal;

@Data
@Builder

public class PaymentResponse {

    private Long paymentId;

    private BigDecimal amount;

    private String transactionId;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;
}