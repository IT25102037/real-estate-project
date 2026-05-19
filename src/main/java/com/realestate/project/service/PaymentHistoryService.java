package com.realestate.project.service;

import com.realestate.project.dto.PaymentHistoryResponse;

import java.util.List;

public interface PaymentHistoryService {

    List<PaymentHistoryResponse>
    getAllPaymentHistory();
}
