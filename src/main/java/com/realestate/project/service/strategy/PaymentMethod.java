package com.realestate.project.service.strategy;

import com.realestate.project.entity.Payment;

public interface PaymentMethod {
    void processPayment(Payment payment);
}
