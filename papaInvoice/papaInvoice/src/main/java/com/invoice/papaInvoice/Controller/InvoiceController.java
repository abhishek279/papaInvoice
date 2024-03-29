package com.invoice.papaInvoice.Controller;
import com.invoice.papaInvoice.Entity.Customer;
import com.invoice.papaInvoice.Entity.DispatchDetail;
import com.invoice.papaInvoice.Entity.Invoice;
import com.invoice.papaInvoice.Entity.InvoiceCreationDto;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Service.EmailService;
import com.invoice.papaInvoice.Service.InvoiceService;
import com.invoice.papaInvoice.Service.PdfService;
import com.invoice.papaInvoice.exception.InvoiceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private PdfService pdfService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailService emailService;
    @GetMapping
    public List<Invoice> list() {
        return invoiceService.listAll();
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@Valid @RequestBody InvoiceCreationDto invoiceDto) {
        Invoice invoice = new Invoice();

        // Set properties from DTO to Invoice entity
        invoice.setProductName(invoiceDto.getProductName());
        invoice.setDescription(invoiceDto.getDescription());
        invoice.setQuantity(invoiceDto.getQuantity());
        invoice.setRate(invoiceDto.getRate());
        invoice.setGstRate(invoiceDto.getGstRate());
        invoice.setTotalAmount(invoiceDto.getTotalAmount());

        // Handle customer association
        Customer customer = customerRepository.findById(invoiceDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + invoiceDto.getCustomerId()));
        invoice.setCustomer(customer);

        // New code to handle dispatch details
        if (invoiceDto.getDispatchDetail() != null) {
            DispatchDetail dispatchDetail = new DispatchDetail();

            // Map fields from DispatchDetailDto to DispatchDetail entity
            InvoiceCreationDto.DispatchDetailDto dto = invoiceDto.getDispatchDetail();
            dispatchDetail.setChallanNo(dto.getChallanNo());
            dispatchDetail.setChallanDate(dto.getChallanDate());
            dispatchDetail.setPlace(dto.getPlace());
            dispatchDetail.setPoNo(dto.getPoNo());
            dispatchDetail.setSize(dto.getSize());
            dispatchDetail.setBoxNumber(dto.getBoxNumber());
            dispatchDetail.setDispatchQty(dto.getDispatchQty());
            dispatchDetail.setTransport(dto.getTransport());
            dispatchDetail.setLrNo(dto.getLrNo());
            dispatchDetail.setLrDate(dto.getLrDate());
            dispatchDetail.setFreight(dto.getFreight());
            dispatchDetail.setDeliveryType(dto.getDeliveryType());

            // Set back-reference from DispatchDetail to Invoice
            dispatchDetail.setInvoice(invoice);
            invoice.setDispatchDetail(dispatchDetail);
        }

        Invoice savedInvoice = invoiceService.save(invoice);
        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/{invoiceId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId) throws IOException {
        Invoice invoice = invoiceService.findById(invoiceId); // Assume you have a method to find an invoice by ID
        ByteArrayInputStream pdfContent = pdfService.generateInvoicePdf(invoice);

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
            Invoice invoice = invoiceService.findById(invoiceId);
            ByteArrayInputStream bis = pdfService.generateInvoicePdf(invoice);
            byte[] pdfBytes = bis.readAllBytes();

            // Define recipient, subject here. Customize as necessary.
            emailService.sendInvoiceEmail(invoice.getCustomer().getEmail(), "Your Invoice", pdfBytes, invoiceId.toString());
            return ResponseEntity.ok("Invoice sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send invoice");
        }
    }


    // Additional endpoints for update and delete
}
