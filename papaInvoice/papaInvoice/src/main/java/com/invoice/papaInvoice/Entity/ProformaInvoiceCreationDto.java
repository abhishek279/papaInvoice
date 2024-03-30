package com.invoice.papaInvoice.Entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public class ProformaInvoiceCreationDto {
    private Long customerId;
    private List<ProductItemDto> productItems;

    // Nested DTO for product items
    public static class ProductItemDto {
        private String productName;
        private String description;
        private int quantity;
        private double rate;
        private double gstRate;

        // Standard getters and setters

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getGstRate() {
            return gstRate;
        }

        public void setGstRate(double gstRate) {
            this.gstRate = gstRate;
        }
    }

    // Standard getters and setters for ProformaInvoiceCreationDto

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<ProductItemDto> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItemDto> productItems) {
        this.productItems = productItems;
    }
}
