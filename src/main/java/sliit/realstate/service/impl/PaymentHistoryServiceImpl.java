package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.PaymentHistoryResponse;
import sliit.realstate.repo.PaymentHistoryRepository;
import sliit.realstate.service.PaymentHistoryService;

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