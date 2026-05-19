package sliit.realstate.dto;

import lombok.Data;
import sliit.realstate.enums.PaymentStatus;

@Data

public class UpdatePaymentStatusRequest {

    private PaymentStatus paymentStatus;
}