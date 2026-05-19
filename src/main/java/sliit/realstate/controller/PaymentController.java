package sliit.realstate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sliit.realstate.dto.CreatePaymentRequest;
import sliit.realstate.dto.PaymentResponse;
import sliit.realstate.enums.PaymentType;
import sliit.realstate.service.PaymentService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(
            @Valid @RequestBody CreatePaymentRequest request
    ) {

        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {

        return paymentService.getAllPayments();
    }

    @GetMapping("/type/{type}")

    public List<PaymentResponse>
    getPaymentsByType(
            @PathVariable PaymentType type
    ) {

        return paymentService
                .getPaymentsByType(type);
    }

}