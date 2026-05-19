package sliit.realstate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sliit.realstate.dto.InvoiceResponse;
import sliit.realstate.service.InvoiceService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor

public class InvoiceController {

    private final InvoiceService
            invoiceService;

    @GetMapping
    public List<InvoiceResponse>
    getAllInvoices() {

        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceResponse getInvoiceById(
            @PathVariable Long id
    ) {

        return invoiceService.getInvoiceById(id);
    }
}