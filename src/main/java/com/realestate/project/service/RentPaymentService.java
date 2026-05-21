package com.realestate.project.service;

import com.realestate.project.dto.CreateRentPaymentRequest;
import com.realestate.project.dto.RentPaymentResponse;
import com.realestate.project.enums.RentStatus;

import java.util.List;

public interface RentPaymentService {

    RentPaymentResponse createRentPayment(
            CreateRentPaymentRequest request
    );

    List<RentPaymentResponse> getAllRentPayments();

    RentPaymentResponse markAsPaid(Long id);

    List<RentPaymentResponse> markOverdueRents();

    List<RentPaymentResponse> getRentPaymentsByStatus(
            RentStatus status
    );
}
