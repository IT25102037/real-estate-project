package sliit.realstate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sliit.realstate.dto.CreateRentPaymentRequest;
import sliit.realstate.dto.RentPaymentResponse;
import sliit.realstate.enums.RentStatus;
import sliit.realstate.service.RentPaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/rent-payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RentPaymentController {

    private final RentPaymentService rentPaymentService;

    @PostMapping
    public RentPaymentResponse createRentPayment(
            @RequestBody CreateRentPaymentRequest request
    ) {
        return rentPaymentService
                .createRentPayment(request);
    }

    @GetMapping
    public List<RentPaymentResponse>
    getAllRentPayments() {
        return rentPaymentService
                .getAllRentPayments();
    }

    @PutMapping("/{id}/mark-paid")
    public RentPaymentResponse markAsPaid(
            @PathVariable Long id
    ) {
        return rentPaymentService.markAsPaid(id);
    }
    @PutMapping("/mark-overdue")
    public List<RentPaymentResponse> markOverdueRents() {
        return rentPaymentService.markOverdueRents();
    }
    @GetMapping("/status/{status}")
    public List<RentPaymentResponse> getRentPaymentsByStatus(
            @PathVariable RentStatus status
    ) {
        return rentPaymentService
                .getRentPaymentsByStatus(status);
    }
}