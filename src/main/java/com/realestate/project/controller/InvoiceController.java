package com.realestate.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.realestate.project.dto.InvoiceResponse;
import com.realestate.project.service.InvoiceService;

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
