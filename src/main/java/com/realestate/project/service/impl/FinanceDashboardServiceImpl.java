package com.realestate.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.realestate.project.dto.FinanceSummaryResponse;
import com.realestate.project.repo.PaymentRepository;
import com.realestate.project.service.FinanceDashboardService;

import com.realestate.project.repo.BankAccountRepository;

@Service
@RequiredArgsConstructor
public class FinanceDashboardServiceImpl implements FinanceDashboardService {

    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public FinanceSummaryResponse getSummary() {

        return FinanceSummaryResponse
                .builder()
                .totalPayments(paymentRepository.countBy())
                .totalRevenue(paymentRepository.getTotalRevenue())
                .rentRevenue(paymentRepository.getRentRevenue())
                .boostRevenue(paymentRepository.getBoostRevenue())
                .totalBankAccounts(bankAccountRepository.count())
                .pendingInvoices(0L)
                .overdueInvoices(0L)
                .build();
    }
}
