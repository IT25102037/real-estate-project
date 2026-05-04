package com.realestate.project.service;

import com.realestate.project.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long id);
    List<PaymentDTO> getAllPayments();
    void deletePayment(Long id);
    List<PaymentDTO> getPaymentsByUserId(Long userId);
}
