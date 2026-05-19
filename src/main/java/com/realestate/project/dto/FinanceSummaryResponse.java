package com.realestate.project.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder

public class FinanceSummaryResponse {

    private Long totalPayments;

    private BigDecimal totalRevenue;

    private BigDecimal rentRevenue;

    private BigDecimal boostRevenue;

    private Long totalBankAccounts;

    private Long pendingInvoices;

    private Long overdueInvoices;
}
