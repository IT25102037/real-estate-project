package com.realestate.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.realestate.project.dto.PaymentHistoryResponse;
import com.realestate.project.repo.PaymentHistoryRepository;
import com.realestate.project.service.PaymentHistoryService;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PaymentHistoryServiceImpl
        implements PaymentHistoryService {

    private final PaymentHistoryRepository
            paymentHistoryRepository;

    @Override
    public List<PaymentHistoryResponse>
    getAllPaymentHistory() {

        return paymentHistoryRepository.findAll()
                .stream()
                .map(history ->
                        PaymentHistoryResponse.builder()
                                .id(history.getId())
                                .paymentId(
                                        history.getPaymentId()
                                )
                                .oldStatus(
                                        history.getOldStatus()
                                )
                                .newStatus(
                                        history.getNewStatus()
                                )
                                .updatedAt(
                                        history.getUpdatedAt()
                                )
                                .build()
                ).toList();
    }
}
