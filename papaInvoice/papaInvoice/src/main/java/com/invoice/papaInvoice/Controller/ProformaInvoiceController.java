package com.invoice.papaInvoice.Controller;


import com.invoice.papaInvoice.Entity.Customer;
import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Entity.ProformaInvoice;
import com.invoice.papaInvoice.Entity.ProformaInvoiceCreationDto;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Service.EmailService;
import com.invoice.papaInvoice.Service.PdfService;
import com.invoice.papaInvoice.Service.ProformaInvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/proformaInvoices")
public class ProformaInvoiceController {

    @Autowired
    private ProformaInvoiceService proformaInvoiceService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<ProformaInvoice> list() {
        return proformaInvoiceService.listAll();
    }

    @PostMapping
    public ResponseEntity<ProformaInvoice> create(@Valid @RequestBody ProformaInvoiceCreationDto dto) {
        ProformaInvoice proformaInvoice = new ProformaInvoice();

        // Set properties from DTO
        proformaInvoice.setProductName(dto.getProductName());
        proformaInvoice.setDescription(dto.getDescription());
        proformaInvoice.setQuantity(dto.getQuantity());
        proformaInvoice.setRate(dto.getRate());
        proformaInvoice.setGstRate(dto.getGstRate());
        proformaInvoice.setTotalAmount(dto.getTotalAmount());

        // Handle customer association
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + dto.getCustomerId()));
        proformaInvoice.setCustomer(customer);

        // Additional fields like dispatch details can be set here as needed

        ProformaInvoice savedProformaInvoice = proformaInvoiceService.save(proformaInvoice);
        return ResponseEntity.ok(savedProformaInvoice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProformaInvoice> getInvoiceById(@PathVariable Long id) {
        ProformaInvoice proformaInvoice = proformaInvoiceService.findById(id);
        return ResponseEntity.ok(proformaInvoice);
    }

    @GetMapping("/{invoiceId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId) throws IOException {
        ProformaInvoice invoice = proformaInvoiceService.findById(invoiceId); // Assume you have a method to find an invoice by ID
        ByteArrayInputStream pdfContent = pdfService.generateProformaInvoicePdf(invoice);

        byte[] bytes = pdfContent.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you can set the filename you wish the user to see
        headers.setContentDispositionFormData("filename", "invoice-" + invoiceId + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }

    @PostMapping("/send/{invoiceId}")
    public ResponseEntity<String> sendInvoice(@PathVariable Long invoiceId) {
        try {
            ProformaInvoice invoice = proformaInvoiceService.findById(invoiceId);
            ByteArrayInputStream bis = pdfService.generateProformaInvoicePdf(invoice);
            byte[] pdfBytes = bis.readAllBytes();

            // Define recipient, subject here. Customize as necessary.
            emailService.sendPerformaInvoiceEmail(invoice.getCustomer().getEmail(), "Your Invoice", pdfBytes, invoiceId.toString());
            return ResponseEntity.ok("Invoice sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send invoice");
        }
    }
}
