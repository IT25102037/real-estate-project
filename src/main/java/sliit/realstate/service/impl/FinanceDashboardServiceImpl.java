package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.FinanceSummaryResponse;
import sliit.realstate.repo.PaymentRepository;
import sliit.realstate.service.FinanceDashboardService;

@Service
@RequiredArgsConstructor

public class FinanceDashboardServiceImpl
        implements FinanceDashboardService {

    private final PaymentRepository
            paymentRepository;

    @Override
    public FinanceSummaryResponse
    getSummary() {

        return FinanceSummaryResponse
                .builder()
                .totalPayments(
                        paymentRepository.countBy()
                )
                .totalRevenue(
                        paymentRepository
                                .getTotalRevenue()
                )
                .rentRevenue(
                        paymentRepository
                                .getRentRevenue()
                )
                .boostRevenue(
                        paymentRepository
                                .getBoostRevenue()
                )
                .build();
    }
}