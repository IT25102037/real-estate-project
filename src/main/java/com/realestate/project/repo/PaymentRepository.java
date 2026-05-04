package com.realestate.project.repo;

import com.realestate.project.entity.Payment;
import com.realestate.project.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByPropertyId(Long propertyId);
    List<Payment> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
