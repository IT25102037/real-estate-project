package sliit.realstate.dto;

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
}