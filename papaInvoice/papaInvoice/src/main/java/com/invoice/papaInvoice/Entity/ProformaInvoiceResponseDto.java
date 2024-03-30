package com.invoice.papaInvoice.Entity;

import java.math.BigDecimal;
import java.util.List;

public class ProformaInvoiceResponseDto {
    private Long id;
    private CustomerDto customer;
    private BigDecimal grandTotal; // New field for grand total


    private List<ProductItemDto> productItems;
    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public List<ProductItemDto> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItemDto> productItems) {
        this.productItems = productItems;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
}


