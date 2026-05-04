package com.realestate.project.service.strategy;

import com.realestate.project.entity.Payment;
import com.realestate.project.entity.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CardPayment implements PaymentMethod {
    @Override
    public void processPayment(Payment payment) {
        // Simulate realistic processing with a 30% failure rate
        double random = Math.random();
        if (payment.getAmount() > 0 && random > 0.30) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
    }
}
