package com.realestate.project.service;

import com.realestate.project.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    List<InvoiceResponse> getAllInvoices();

    InvoiceResponse getInvoiceById(Long id);
}
