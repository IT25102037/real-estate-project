package com.realestate.project.dto;

import lombok.Data;

@Data

public class BankAccountDTO {

    private Long id;

    private String bankName;

    private String accountHolderName;

    private String accountNumber;

    private String branchName;

    private Long userId;
}
