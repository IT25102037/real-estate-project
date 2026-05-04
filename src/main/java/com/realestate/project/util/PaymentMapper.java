package com.realestate.project.util;

import com.realestate.project.dto.PaymentDTO;
import com.realestate.project.entity.Payment;

public class PaymentMapper {

    public static PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setDate(payment.getDate());
        dto.setStatus(payment.getStatus());
        dto.setPaymentType(payment.getPaymentType());
        
        if (payment.getUser() != null) {
            dto.setUserId(payment.getUser().getId());
        }
        
        if (payment.getProperty() != null) {
            dto.setPropertyId(payment.getProperty().getId());
        }

        return dto;
    }

    public static Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setAmount(dto.getAmount());
        payment.setDate(dto.getDate());
        payment.setStatus(dto.getStatus());
        payment.setPaymentType(dto.getPaymentType());
        
        // Note: User and Property associations need to be set in the service layer
        // by fetching the corresponding entities using userId and propertyId

        return payment;
    }
}
