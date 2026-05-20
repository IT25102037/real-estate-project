package com.realestate.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.BoostPayment;

public interface BoostPaymentRepository
        extends JpaRepository<BoostPayment, Long> {
}
