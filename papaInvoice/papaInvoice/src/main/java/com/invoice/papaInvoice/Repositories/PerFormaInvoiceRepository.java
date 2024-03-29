package com.invoice.papaInvoice.Repositories;


import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Entity.ProformaInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerFormaInvoiceRepository extends JpaRepository<ProformaInvoice, Long> {
}
