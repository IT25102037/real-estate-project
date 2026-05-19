package com.realestate.project.service;

import com.realestate.project.dto.CreatePaymentRequest;
import com.realestate.project.dto.PaymentResponse;
import com.realestate.project.dto.UpdatePaymentStatusRequest;
import com.realestate.project.enums.PaymentType;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(
            CreatePaymentRequest request
    );

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse>
    getPaymentsByType(
            PaymentType paymentType
    );



    PaymentResponse updatePaymentStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request
    );
}
