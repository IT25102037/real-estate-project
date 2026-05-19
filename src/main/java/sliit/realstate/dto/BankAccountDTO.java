package sliit.realstate.dto;

import lombok.Data;

@Data

public class BankAccountDTO {

    private String bankName;

    private String accountHolderName;

    private String accountNumber;

    private String branchName;

    private Long userId;
}