package sliit.realstate.service;

import sliit.realstate.dto.PaymentHistoryResponse;

import java.util.List;

public interface PaymentHistoryService {

    List<PaymentHistoryResponse>
    getAllPaymentHistory();
}