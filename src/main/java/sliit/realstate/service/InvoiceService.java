package sliit.realstate.service;

import sliit.realstate.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    List<InvoiceResponse> getAllInvoices();

    InvoiceResponse getInvoiceById(Long id);
}