package com.realestate.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.realestate.project.entity.Payment;
import com.realestate.project.enums.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    // Dashboard Summary

    Long countBy();

    @Query("""
            SELECT COALESCE(SUM(p.amount),0)
            FROM Payment p
            """)
    BigDecimal getTotalRevenue();

    @Query("""
            SELECT COALESCE(SUM(p.amount),0)
            FROM Payment p
            WHERE p.paymentType = 'MONTHLY_RENT'
            """)
    BigDecimal getRentRevenue();

    @Query("""
            SELECT COALESCE(SUM(p.amount),0)
            FROM Payment p
            WHERE p.paymentType = 'POST_BOOST'
            """)
    BigDecimal getBoostRevenue();

    // Filter By Payment Type

    List<Payment> findByPaymentType(
            PaymentType paymentType
    );

    // Filter By User ID

    List<Payment> findByUserId(
            Long userId
    );
}
