package sliit.realstate.service;

import sliit.realstate.dto.BoostPaymentResponse;
import sliit.realstate.dto.CreateBoostPaymentRequest;

import java.util.List;

public interface BoostPaymentService {

    BoostPaymentResponse createBoostPayment(
            CreateBoostPaymentRequest request
    );

    List<BoostPaymentResponse> getAllBoostPayments();

    BoostPaymentResponse expireBoostPayment(Long id);

    List<BoostPaymentResponse> autoExpireBoosts();
}