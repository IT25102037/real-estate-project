package com.realestate.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.realestate.project.dto.BankAccountDTO;
import com.realestate.project.service.BankAccountService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor

public class BankAccountController {

    private final BankAccountService
            bankAccountService;

    @PostMapping
    public BankAccountDTO addBankAccount(
            @RequestBody BankAccountDTO dto
    ) {

        return bankAccountService
                .addBankAccount(dto);
    }

    @GetMapping
    public List<BankAccountDTO>
    getAllBankAccounts() {

        return bankAccountService
                .getAllBankAccounts();
    }
}
