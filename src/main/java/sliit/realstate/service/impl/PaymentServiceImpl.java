package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.CreatePaymentRequest;
import sliit.realstate.dto.PaymentResponse;
import sliit.realstate.dto.UpdatePaymentStatusRequest;
import sliit.realstate.entity.Invoice;
import sliit.realstate.entity.Payment;
import sliit.realstate.entity.PaymentHistory;
import sliit.realstate.enums.PaymentStatus;
import sliit.realstate.enums.PaymentType;
import sliit.realstate.repo.InvoiceRepository;
import sliit.realstate.repo.PaymentHistoryRepository;
import sliit.realstate.repo.PaymentRepository;
import sliit.realstate.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .propertyId(request.getPropertyId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentType(request.getPaymentType())
                .paymentStatus(PaymentStatus.COMPLETED)
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        PaymentHistory history = PaymentHistory.builder()
                .paymentId(savedPayment.getId())
                .oldStatus("PENDING")
                .newStatus("COMPLETED")
                .updatedAt(LocalDateTime.now())
                .build();

        paymentHistoryRepository.save(history);

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + System.currentTimeMillis())
                .paymentId(savedPayment.getId())
                .totalAmount(savedPayment.getAmount())
                .issuedDate(LocalDateTime.now())
                .build();

        invoiceRepository.save(invoice);

        return mapToPaymentResponse(savedPayment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToPaymentResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByType(PaymentType paymentType) {
        return paymentRepository.findByPaymentType(paymentType)
                .stream()
                .map(this::mapToPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse updatePaymentStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request
    ) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        String oldStatus = payment.getPaymentStatus().name();

        payment.setPaymentStatus(request.getPaymentStatus());

        Payment updatedPayment = paymentRepository.save(payment);

        PaymentHistory history = PaymentHistory.builder()
                .paymentId(updatedPayment.getId())
                .oldStatus(oldStatus)
                .newStatus(request.getPaymentStatus().name())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentHistoryRepository.save(history);

        return mapToPaymentResponse(updatedPayment);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .paymentType(payment.getPaymentType())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }
}