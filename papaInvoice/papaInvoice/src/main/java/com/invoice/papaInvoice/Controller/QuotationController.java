package com.invoice.papaInvoice.Controller;

import com.invoice.papaInvoice.Entity.*;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import com.invoice.papaInvoice.Repositories.QuotationRepository;
import com.invoice.papaInvoice.Service.EmailService;
import com.invoice.papaInvoice.Service.PdfService;
import com.invoice.papaInvoice.Service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<QuotationListDTO>> list() {
        List<Quotation> quotations = quotationService.listAll();
        List<QuotationListDTO> quotationDTOs = quotations.stream()
                .map(this::convertToDTO) // Convert each Quotation to QuotationListDTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(quotationDTOs);
    }

    private QuotationListDTO convertToDTO(Quotation quotation) {
        QuotationListDTO dto = new QuotationListDTO();
        dto.setId(quotation.getId());
        dto.setCustomerName(quotation.getCustomer().getName()); // Simplified, adjust as needed

        List<QuotationItemDTO> itemDTOs = quotation.getItems().stream()
                .map(item -> {
                    QuotationItemDTO itemDTO = new QuotationItemDTO();
                    itemDTO.setCapSize(item.getCapSize());
                    itemDTO.setWadThickness(item.getWadThickness());
                    itemDTO.setStructure(item.getStructure());
                    itemDTO.setRatePer100(item.getRatePer100());
                    itemDTO.setMoq(item.getMoq());
                    return itemDTO;
                })
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }
    // POST endpoint to create a new quotation
    @PostMapping
    public ResponseEntity<Quotation> createQuotation(@RequestBody QuotationCreationDto quotationDto) {
        if (quotationDto.getCustomerId() == null) {
            throw new IllegalArgumentException("Quotation must have a valid customer ID.");
        }

        Customer customer = customerRepository.findById(quotationDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + quotationDto.getCustomerId()));

        Quotation quotation = new Quotation();
        quotation.setCustomer(customer);

        for (QuotationCreationDto.QuotationItemDto itemDto : quotationDto.getItems()) {
            QuotationItem item = new QuotationItem();
            item.setCapSize(itemDto.getCapSize());
            item.setWadThickness(itemDto.getWadThickness());
            item.setStructure(itemDto.getStructure());
            item.setRatePer100(itemDto.getRatePer100());
            item.setMoq(itemDto.getMoq());
            quotation.addItem(item);
        }

        Quotation savedQuotation = quotationService.save(quotation);
        return ResponseEntity.ok(savedQuotation);
    }


    // GET endpoint to retrieve a quotation by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Quotation> getQuotationById(@PathVariable Long id) {
        Quotation quotation = quotationService.findById(id);
        return ResponseEntity.ok(quotation);
    }

    @GetMapping("/{quotationId}/pdf")
    public ResponseEntity<byte[]> getQuotationPdf(@PathVariable Long quotationId) throws Exception {
        Quotation quotation = quotationService.findById(quotationId);
        ByteArrayInputStream pdfContent = pdfService.generateQuotationPdf(quotation);

        byte[] bytes = pdfContent.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "quotation-" + quotationId + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }

    @PostMapping("/send/{quotationId}")
    public ResponseEntity<String> sendQuotation(@PathVariable Long quotationId) {
        try {
            Quotation quotation = quotationService.findById(quotationId);
            ByteArrayInputStream bis = pdfService.generateQuotationPdf(quotation);
            byte[] pdfBytes = bis.readAllBytes();

            // Define recipient, subject here. Customize as necessary.
            emailService.sendQuotationEmail(quotation.getCustomer().getEmail(), "Your Quotation", pdfBytes, "quotation-" + quotationId.toString());
            return ResponseEntity.ok("Quotation sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send quotation");
        }
    }



    // Additional endpoints as needed
}
