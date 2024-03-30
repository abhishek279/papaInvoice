package com.invoice.papaInvoice.Service;


import com.invoice.papaInvoice.Entity.Customer;
import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Entity.ProformaInvoice;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Repositories.InvoiceRepository;
import com.invoice.papaInvoice.Repositories.PerFormaInvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProformaInvoiceService {
    @Autowired
    private PerFormaInvoiceRepository perFormaInvoiceRepository;
    @Autowired
    private CustomerRepository customerRepository; //

    public List<ProformaInvoice> listAll() {
        return perFormaInvoiceRepository.findAll();
    }

    @Transactional
    public ProformaInvoice save(ProformaInvoice perProformaInvoice) {
        // Check if the invoice's customer is null or if the customer's ID is null
        if (perProformaInvoice.getCustomer() == null || perProformaInvoice.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Invoice must have a valid customer.");
        }

        Customer customer = customerRepository.findById(perProformaInvoice.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + perProformaInvoice.getCustomer().getId()));

        // Now that we have a valid customer, we can safely set it on the invoice
        perProformaInvoice.setCustomer(customer);

        return perFormaInvoiceRepository.save(perProformaInvoice);
    }


    public ProformaInvoice findById(Long id) {
        // The findById method returns an Optional, so we use orElseThrow to throw an exception if the invoice is not found
        return perFormaInvoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
    }
    // Additional methods for update and delete
}

