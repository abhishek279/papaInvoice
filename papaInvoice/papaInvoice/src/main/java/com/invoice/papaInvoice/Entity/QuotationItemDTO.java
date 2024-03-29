package com.invoice.papaInvoice.Entity;


import java.math.BigDecimal;

public class QuotationItemDTO {
    private String capSize;
    private String wadThickness;
    private String structure;
    private BigDecimal ratePer100;
    private Integer moq;

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

    // Getters and Setters
}