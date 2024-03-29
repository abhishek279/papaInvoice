package com.invoice.papaInvoice.Entity;

import java.util.List;

public class QuotationListDTO {
    private Long id;
    private String customerName;
    private List<QuotationItemDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<QuotationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemDTO> items) {
        this.items = items;
    }

    // Getters and Setters
}