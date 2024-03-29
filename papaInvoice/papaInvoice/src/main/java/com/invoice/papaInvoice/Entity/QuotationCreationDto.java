package com.invoice.papaInvoice.Entity;

import java.math.BigDecimal;
import java.util.List;

public class QuotationCreationDto {

    private Long customerId;
    private String subject;
    private String notes;
    private List<QuotationItemDto> items; // A list to hold the item DTOs
    // Standard getters and setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<QuotationItemDto> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemDto> items) {
        this.items = items;
    }

    // Nested DTO for Quotation items
    public static class QuotationItemDto {

        private String capSize;
        private String wadThickness;
        private String structure;
        private BigDecimal ratePer100;
        private Integer moq;

        // Getters and setters for each property


        public String getCapSize() {
            return capSize;
        }

        public void setCapSize(String capSize) {
            this.capSize = capSize;
        }

        public String getWadThickness() {
            return wadThickness;
        }

        public void setWadThickness(String wadThickness) {
            this.wadThickness = wadThickness;
        }

        public String getStructure() {
            return structure;
        }

        public void setStructure(String structure) {
            this.structure = structure;
        }

        public BigDecimal getRatePer100() {
            return ratePer100;
        }

        public void setRatePer100(BigDecimal ratePer100) {
            this.ratePer100 = ratePer100;
        }

        public Integer getMoq() {
            return moq;
        }

        public void setMoq(Integer moq) {
            this.moq = moq;
        }
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "QuotationCreationDto{" +
                "customerId=" + customerId +
                ", subject='" + subject + '\'' +
                ", notes='" + notes + '\'' +
                ", items=" + items +
                '}';
    }
}
