package sliit.realstate.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.realstate.entity.Invoice;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {
}