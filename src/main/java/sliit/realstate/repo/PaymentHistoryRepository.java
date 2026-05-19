package sliit.realstate.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.realstate.entity.PaymentHistory;

public interface PaymentHistoryRepository
        extends JpaRepository<PaymentHistory, Long> {
}
