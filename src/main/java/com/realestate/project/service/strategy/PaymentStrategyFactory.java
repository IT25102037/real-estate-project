package com.realestate.project.service.strategy;

import com.realestate.project.entity.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    @Autowired
    private CardPayment cardPayment;

    @Autowired
    private CashPayment cashPayment;

    @Autowired
    private OnlinePayment onlinePayment;

    public PaymentMethod getPaymentMethod(PaymentType paymentType) {
        if (paymentType == null) {
            throw new IllegalArgumentException("PaymentType cannot be null");
        }
        
        switch (paymentType) {
            case CARD:
                return cardPayment;
            case CASH:
                return cashPayment;
            case ONLINE:
            case DIALOG_EZ_CASH:
                return onlinePayment;
            default:
                throw new IllegalArgumentException("Unsupported PaymentType: " + paymentType);
        }
    }
}
