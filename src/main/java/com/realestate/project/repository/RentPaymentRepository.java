package com.realestate.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.RentPayment;
import com.realestate.project.enums.RentStatus;

import java.util.List;

public interface RentPaymentRepository
        extends JpaRepository<RentPayment, Long> {

    List<RentPayment> findByStatus(
            RentStatus status
    );
}
