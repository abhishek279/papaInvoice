package com.invoice.papaInvoice.Repositories;

import com.invoice.papaInvoice.Entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    @Query("SELECT q FROM Quotation q JOIN FETCH q.items")
    List<Quotation> findAllWithItems();
}