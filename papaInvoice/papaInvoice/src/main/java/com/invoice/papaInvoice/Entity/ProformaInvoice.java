package com.invoice.papaInvoice.Entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProformaInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "proformaInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItem> productItems = new ArrayList<>();

    private BigDecimal grandTotal; // New field for grand total

    // Additional proforma-specific fields and methods
    // ...

    public void addProductItem(ProductItem item) {
        productItems.add(item);
        item.setProformaInvoice(this);
    }

    public void removeProductItem(ProductItem item) {
        productItems.remove(item);
        item.setProformaInvoice(null);
    }

    public void updateGrandTotal() {
        grandTotal = productItems.stream()
                .map(ProductItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Standard getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
