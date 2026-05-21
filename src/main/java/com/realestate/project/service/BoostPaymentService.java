package com.realestate.project.service;

import com.realestate.project.dto.BoostPaymentResponse;
import com.realestate.project.dto.CreateBoostPaymentRequest;

import java.util.List;

public interface BoostPaymentService {

    BoostPaymentResponse createBoostPayment(
            CreateBoostPaymentRequest request
    );

    List<BoostPaymentResponse> getAllBoostPayments();

    BoostPaymentResponse expireBoostPayment(Long id);

    List<BoostPaymentResponse> autoExpireBoosts();
}
