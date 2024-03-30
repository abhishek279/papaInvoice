package com.invoice.papaInvoice.Controller;


import com.invoice.papaInvoice.Entity.*;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Service.EmailService;
import com.invoice.papaInvoice.Service.PdfService;
import com.invoice.papaInvoice.Service.ProformaInvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ProformaInvoiceResponseDto>> getAllProformaInvoices() {
        List<ProformaInvoice> invoices = proformaInvoiceService.listAll();
        List<ProformaInvoiceResponseDto> dtoList = invoices.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
    private ProformaInvoiceResponseDto convertToDto(ProformaInvoice invoice) {
        ProformaInvoiceResponseDto dto = new ProformaInvoiceResponseDto();
        dto.setId(invoice.getId());

        // Convert Customer
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(invoice.getCustomer().getId());
        customerDto.setName(invoice.getCustomer().getName());
        // Map other customer fields as necessary
        dto.setCustomer(customerDto);

        // Convert ProductItems
        List<ProductItemDto> productItemDtos = invoice.getProductItems().stream().map(item -> {
            ProductItemDto itemDto = new ProductItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductName(item.getProductName());
            itemDto.setDescription(item.getDescription());
            itemDto.setRate(item.getRate());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setGstRate(item.getGstRate());
            // Map other product item fields as necessary
            return itemDto;
        }).collect(Collectors.toList());
        dto.setGrandTotal(invoice.getGrandTotal());

        dto.setProductItems(productItemDtos);

        // Set other fields as necessary
        return dto;
    }




    @PostMapping
    public ResponseEntity<ProformaInvoice> create(@Valid @RequestBody ProformaInvoiceCreationDto dto) {
        ProformaInvoice proformaInvoice = new ProformaInvoice();
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + dto.getCustomerId()));
        proformaInvoice.setCustomer(customer);

        for (ProformaInvoiceCreationDto.ProductItemDto itemDto : dto.getProductItems()) {
            ProductItem item = new ProductItem();
            item.setProductName(itemDto.getProductName());
            item.setDescription(itemDto.getDescription());
            item.setQuantity(itemDto.getQuantity());
            item.setRate(itemDto.getRate());
            item.setGstRate(itemDto.getGstRate());
            BigDecimal totalAmount = BigDecimal.valueOf(Math.round((itemDto.getRate()/1000 * itemDto.getQuantity()) * (1 + itemDto.getGstRate() / 100)));
            item.setTotalAmount(totalAmount);
            proformaInvoice.addProductItem(item);
        }
        proformaInvoice.updateGrandTotal(); // Calculate the grand total

        ProformaInvoice savedProformaInvoice = proformaInvoiceService.save(proformaInvoice);
        return new ResponseEntity<>(savedProformaInvoice, HttpStatus.CREATED);
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
