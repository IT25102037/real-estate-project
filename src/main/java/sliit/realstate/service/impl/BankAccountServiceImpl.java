package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.BankAccountDTO;
import sliit.realstate.entity.BankAccount;
import sliit.realstate.repo.BankAccountRepository;
import sliit.realstate.service.BankAccountService;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BankAccountServiceImpl
        implements BankAccountService {

    private final BankAccountRepository
            bankAccountRepository;

    @Override
    public BankAccountDTO addBankAccount(
            BankAccountDTO dto
    ) {

        BankAccount account =
                BankAccount.builder()
                        .bankName(dto.getBankName())
                        .accountHolderName(
                                dto.getAccountHolderName()
                        )
                        .accountNumber(
                                dto.getAccountNumber()
                        )
                        .branchName(dto.getBranchName())
                        .userId(dto.getUserId())
                        .build();

        BankAccount savedAccount =
                bankAccountRepository.save(account);

        BankAccountDTO response =
                new BankAccountDTO();

        response.setBankName(
                savedAccount.getBankName()
        );

        response.setAccountHolderName(
                savedAccount.getAccountHolderName()
        );

        response.setAccountNumber(
                savedAccount.getAccountNumber()
        );

        response.setBranchName(
                savedAccount.getBranchName()
        );

        response.setUserId(
                savedAccount.getUserId()
        );

        return response;
    }

    @Override
    public List<BankAccountDTO>
    getAllBankAccounts() {

        return bankAccountRepository.findAll()
                .stream()
                .map(account -> {

                    BankAccountDTO dto =
                            new BankAccountDTO();

                    dto.setBankName(
                            account.getBankName()
                    );

                    dto.setAccountHolderName(
                            account.getAccountHolderName()
                    );

                    dto.setAccountNumber(
                            account.getAccountNumber()
                    );

                    dto.setBranchName(
                            account.getBranchName()
                    );

                    dto.setUserId(
                            account.getUserId()
                    );

                    return dto;

                }).toList();
    }
}