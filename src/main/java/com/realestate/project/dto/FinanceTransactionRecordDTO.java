package com.realestate.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinanceTransactionRecordDTO {

    private Long id;
    private String payment;
    private String paymentHistory;
    private String invoice;
    private String home;
    private String bankAccount;
}
