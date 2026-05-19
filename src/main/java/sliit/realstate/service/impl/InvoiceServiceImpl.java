package sliit.realstate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.realstate.dto.InvoiceResponse;
import sliit.realstate.entity.Invoice;
import sliit.realstate.exception.ResourceNotFoundException;
import sliit.realstate.repo.InvoiceRepository;
import sliit.realstate.service.InvoiceService;

import java.util.List;

@Service
@RequiredArgsConstructor

public class InvoiceServiceImpl
        implements InvoiceService {

    private final InvoiceRepository
            invoiceRepository;

    @Override
    public List<InvoiceResponse>
    getAllInvoices() {

        return invoiceRepository.findAll()
                .stream()
                .map(invoice ->
                        InvoiceResponse.builder()
                                .invoiceId(invoice.getId())
                                .invoiceNumber(
                                        invoice.getInvoiceNumber()
                                )
                                .totalAmount(
                                        invoice.getTotalAmount()
                                )
                                .issuedDate(
                                        invoice.getIssuedDate()
                                )
                                .paymentId(
                                        invoice.getPaymentId()
                                )
                                .build()
                ).toList();
    }

    @Override
    public InvoiceResponse getInvoiceById(
            Long id
    ) {

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invoice not found"
                                )
                        );

        return InvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .invoiceNumber(
                        invoice.getInvoiceNumber()
                )
                .totalAmount(
                        invoice.getTotalAmount()
                )
                .issuedDate(
                        invoice.getIssuedDate()
                )
                .paymentId(
                        invoice.getPaymentId()
                )
                .build();
    }
}