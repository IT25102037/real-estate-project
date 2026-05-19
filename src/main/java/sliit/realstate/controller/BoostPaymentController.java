package sliit.realstate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sliit.realstate.dto.BoostPaymentResponse;
import sliit.realstate.dto.CreateBoostPaymentRequest;
import sliit.realstate.service.BoostPaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/boost-payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoostPaymentController {

    private final BoostPaymentService boostPaymentService;

    @PostMapping
    public BoostPaymentResponse createBoostPayment(
            @RequestBody CreateBoostPaymentRequest request
    ) {
        return boostPaymentService
                .createBoostPayment(request);
    }

    @GetMapping
    public List<BoostPaymentResponse>
    getAllBoostPayments() {
        return boostPaymentService
                .getAllBoostPayments();
    }

    @PutMapping("/{id}/expire")
    public BoostPaymentResponse expireBoostPayment(
            @PathVariable Long id
    ) {
        return boostPaymentService
                .expireBoostPayment(id);
    }
    @PutMapping("/auto-expire")
    public List<BoostPaymentResponse>
    autoExpireBoosts() {

        return boostPaymentService
                .autoExpireBoosts();
    }
}