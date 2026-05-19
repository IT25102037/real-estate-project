package com.realestate.project.service;

import com.realestate.project.dto.BankAccountDTO;

import java.util.List;

public interface BankAccountService {

    BankAccountDTO addBankAccount(
            BankAccountDTO dto
    );

    List<BankAccountDTO> getAllBankAccounts();
}
