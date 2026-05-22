package com.realestate.project.dto;

import lombok.Data;
import com.realestate.project.enums.PaymentStatus;

@Data

public class UpdatePaymentStatusRequest {

    private PaymentStatus paymentStatus;
}
