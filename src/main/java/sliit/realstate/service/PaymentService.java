package sliit.realstate.service;

import sliit.realstate.dto.CreatePaymentRequest;
import sliit.realstate.dto.PaymentResponse;
import sliit.realstate.dto.UpdatePaymentStatusRequest;
import sliit.realstate.enums.PaymentType;

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
