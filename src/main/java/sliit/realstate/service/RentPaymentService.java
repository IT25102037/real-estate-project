package sliit.realstate.service;

import sliit.realstate.dto.CreateRentPaymentRequest;
import sliit.realstate.dto.RentPaymentResponse;
import sliit.realstate.enums.RentStatus;

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