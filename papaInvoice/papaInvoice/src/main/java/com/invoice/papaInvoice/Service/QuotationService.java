package com.invoice.papaInvoice.Service;

import com.invoice.papaInvoice.Entity.Quotation;
import com.invoice.papaInvoice.Repositories.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationService {
    @Autowired
    private QuotationRepository quotationRepository;

    public Quotation save(Quotation quotation) {
        return quotationRepository.save(quotation);
    }

    public Quotation findById(Long id) {
        return quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found with id " + id));
    }

    public List<Quotation> listAll() {
        return quotationRepository.findAllWithItems();
    }
    // Additional methods as needed
}
