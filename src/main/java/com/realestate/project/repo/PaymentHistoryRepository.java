package com.realestate.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.PaymentHistory;

public interface PaymentHistoryRepository
        extends JpaRepository<PaymentHistory, Long> {
}
