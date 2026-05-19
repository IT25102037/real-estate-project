package sliit.realstate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sliit.realstate.dto.PaymentHistoryResponse;
import sliit.realstate.service.PaymentHistoryService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/payment-history")
@RequiredArgsConstructor

public class PaymentHistoryController {

    private final PaymentHistoryService
            paymentHistoryService;

    @GetMapping
    public List<PaymentHistoryResponse>
    getAllPaymentHistory() {

        return paymentHistoryService
                .getAllPaymentHistory();
    }
}