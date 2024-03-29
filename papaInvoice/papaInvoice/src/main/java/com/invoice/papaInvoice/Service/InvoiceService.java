package com.invoice.papaInvoice.Service;



import com.invoice.papaInvoice.Entity.Customer;
import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Repositories.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CustomerRepository customerRepository; //

    public List<Invoice> listAll() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public Invoice save(Invoice invoice) {
        // Check if the invoice's customer is null or if the customer's ID is null
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Invoice must have a valid customer.");
        }

        Customer customer = customerRepository.findById(invoice.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + invoice.getCustomer().getId()));

        // Now that we have a valid customer, we can safely set it on the invoice
        invoice.setCustomer(customer);

        return invoiceRepository.save(invoice);
    }


    public Invoice findById(Long id) {
        // The findById method returns an Optional, so we use orElseThrow to throw an exception if the invoice is not found
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
    }
    // Additional methods for update and delete
}

