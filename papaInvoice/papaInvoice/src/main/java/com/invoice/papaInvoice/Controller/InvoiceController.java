package com.invoice.papaInvoice.Controller;
import com.invoice.papaInvoice.Entity.*;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.listAll();
        List<InvoiceDto> invoiceDtos = invoices.stream()
                .map(this::convertToInvoiceDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(invoiceDtos, HttpStatus.OK);
    }


    private InvoiceDto convertToInvoiceDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getId());
        dto.setCustomerName(invoice.getCustomer().getName());
        dto.setCustomerAddress(invoice.getCustomer().getAddress());

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

        DispatchDetail dispatchDetail = invoice.getDispatchDetail();
        if (dispatchDetail != null) {
            InvoiceDto.DispatchDetailDto detailDto = new InvoiceDto.DispatchDetailDto();
            detailDto.setChallanNo(dispatchDetail.getChallanNo());
            detailDto.setChallanDate(dispatchDetail.getChallanDate());
            detailDto.setPlace(dispatchDetail.getPlace());
            detailDto.setPoNo(dispatchDetail.getPoNo());
            detailDto.setSize(dispatchDetail.getSize());
            detailDto.setBoxNumber(dispatchDetail.getBoxNumber());
            detailDto.setDispatchQty(dispatchDetail.getDispatchQty());
            detailDto.setTransport(dispatchDetail.getTransport());
            detailDto.setLrNo(dispatchDetail.getLrNo());
            detailDto.setLrDate(dispatchDetail.getLrDate());
            detailDto.setFreight(dispatchDetail.getFreight());
            detailDto.setDeliveryType(dispatchDetail.getDeliveryType());
            // Set additional fields...
            dto.setDispatchDetail(detailDto);
        }

        // Set other fields as necessary...

        return dto;
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@Valid @RequestBody InvoiceCreationDto invoiceDto) {
        Invoice invoice = new Invoice();
        Customer customer = customerRepository.findById(invoiceDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + invoiceDto.getCustomerId()));
        invoice.setCustomer(customer);

        for (InvoiceCreationDto.ProductItemDto itemDto: invoiceDto.getProductItems()) {
            ProductItem item = new ProductItem();
            item.setProductName(itemDto.getProductName());
            item.setDescription(itemDto.getDescription());
            item.setQuantity(itemDto.getQuantity());
            item.setRate(itemDto.getRate());
            item.setGstRate(itemDto.getGstRate());
            BigDecimal totalAmount = BigDecimal.valueOf(Math.round((itemDto.getRate()/1000 * itemDto.getQuantity()) * (1 + itemDto.getGstRate() / 100)));
            item.setTotalAmount(totalAmount);
            invoice.addProductItem(item);
        }


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
        invoice.updateGrandTotal(); // Calculate the grand total


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
