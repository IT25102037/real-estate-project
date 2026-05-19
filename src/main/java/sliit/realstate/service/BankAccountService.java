package sliit.realstate.service;

import sliit.realstate.dto.BankAccountDTO;

import java.util.List;

public interface BankAccountService {

    BankAccountDTO addBankAccount(
            BankAccountDTO dto
    );

    List<BankAccountDTO> getAllBankAccounts();
}