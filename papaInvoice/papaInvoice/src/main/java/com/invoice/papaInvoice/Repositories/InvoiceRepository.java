package com.invoice.papaInvoice.Repositories;


import com.invoice.papaInvoice.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
