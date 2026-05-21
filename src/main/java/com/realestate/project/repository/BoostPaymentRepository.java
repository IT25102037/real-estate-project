package com.realestate.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.BoostPayment;

public interface BoostPaymentRepository
        extends JpaRepository<BoostPayment, Long> {
}
