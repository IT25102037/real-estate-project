package com.realestate.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realestate.project.entity.Invoice;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {
}
