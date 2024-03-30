package com.invoice.papaInvoice.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Association to Customer entity

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItem> productItems = new ArrayList<>();

    private String productName;
    private String description;
    private int quantity;
    private double rate;

    private double gstRate;
    private BigDecimal totalAmount;

    private BigDecimal grandTotal; // New field for grand total


    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    private DispatchDetail dispatchDetail;

    public void addProductItem(ProductItem item) {
        productItems.add(item);
        item.setInvoice(this);
    }

    public void removeProductItem(ProductItem item) {
        productItems.remove(item);
        item.setInvoice(null);
    }

    public void updateGrandTotal() {
        grandTotal = productItems.stream()
                .map(ProductItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and setters for all fields, including customer

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    public DispatchDetail getDispatchDetail() {
        return dispatchDetail;
    }

    public void setDispatchDetail(DispatchDetail dispatchDetail) {
        this.dispatchDetail = dispatchDetail;
    }



    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
}
