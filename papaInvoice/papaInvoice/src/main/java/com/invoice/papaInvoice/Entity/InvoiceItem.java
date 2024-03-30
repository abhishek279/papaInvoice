package com.invoice.papaInvoice.Entity;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item description is required.")
    private String description;

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

    @NotNull(message = "Rate is required.")
    @DecimalMin(value = "0.00", message = "Rate must not be negative.")
    @Digits(integer = 10, fraction = 2, message = "Rate format is invalid.")
    private BigDecimal rate = BigDecimal.ZERO; // Initialize rate to BigDecimal.ZERO

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.00", message = "Amount must not be negative.")
    @Digits(integer = 10, fraction = 2, message = "Amount format is invalid.")
    private BigDecimal amount = BigDecimal.ZERO; // Initialize amount to BigDecimal.ZERO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    // Default constructor
    public InvoiceItem() {
        // Fields are already initialized upon declaration
    }

    // Custom constructor for convenience
    public InvoiceItem(String description, Integer quantity, BigDecimal rate) {
        this.description = description;
        this.quantity = quantity;
        // Protect against null values explicitly, even though we default to BigDecimal.ZERO
        this.rate = rate != null ? rate : BigDecimal.ZERO;
        calculateAmount(); // Ensures amount is calculated and set, using the rate and quantity provided
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateAmount(); // Recalculate amount whenever quantity changes
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate != null ? rate : BigDecimal.ZERO;
        calculateAmount(); // Recalculate amount whenever rate changes
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount : BigDecimal.ZERO;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // Utility method to calculate the amount
    public void calculateAmount() {
        // Given that rate and quantity are ensured not to be null, direct multiplication is safe
        this.amount = this.rate.multiply(BigDecimal.valueOf(this.quantity));
    }

    // Ensure amount is recalculated before persisting or updating
    @PrePersist
    @PreUpdate
    private void prePersistPreUpdate() {
        calculateAmount();
    }

    // Override equals, hashCode, and toString methods if necessary
}