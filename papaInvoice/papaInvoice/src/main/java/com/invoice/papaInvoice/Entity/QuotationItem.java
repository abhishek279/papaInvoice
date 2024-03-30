package com.invoice.papaInvoice.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class QuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String capSize;

    @Column(nullable = false)
    private String wadThickness;

    @Column(nullable = false)
    private String structure;

    @Column(nullable = false)
    private BigDecimal ratePer100;

    @Column(nullable = false)
    private Integer moq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private Quotation quotation;

    // Standard getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    // Convenience methods
    public BigDecimal calculateTotal() {
        return ratePer100.multiply(new BigDecimal(moq));
    }

    // toString method
    @Override
    public String toString() {
        return "QuotationItem{" +
                "id=" + id +
                ", capSize='" + capSize + '\'' +
                ", wadThickness='" + wadThickness + '\'' +
                ", structure='" + structure + '\'' +
                ", ratePer100=" + ratePer100 +
                ", moq=" + moq +
                '}';
    }

    // Add hashCode and equals methods if you want to ensure proper comparison between QuotationItem instances
    // Add @PrePersist and @PreUpdate hooks if you have any logic that needs to run before saving or updating
}
