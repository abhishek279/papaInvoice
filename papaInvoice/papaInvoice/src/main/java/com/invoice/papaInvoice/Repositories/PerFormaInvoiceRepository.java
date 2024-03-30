package com.invoice.papaInvoice.Repositories;


import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Entity.ProformaInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PerFormaInvoiceRepository extends JpaRepository<ProformaInvoice, Long> {

    @Query("SELECT p FROM ProformaInvoice p JOIN FETCH p.customer JOIN FETCH p.productItems")
    List<ProformaInvoice> findAllWithDetails();
}
