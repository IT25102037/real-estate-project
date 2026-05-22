package com.realestate.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.BankAccount;

public interface BankAccountRepository
        extends JpaRepository<BankAccount, Long> {
}
