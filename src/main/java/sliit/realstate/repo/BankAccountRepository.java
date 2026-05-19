package sliit.realstate.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.realstate.entity.BankAccount;

public interface BankAccountRepository
        extends JpaRepository<BankAccount, Long> {
}
