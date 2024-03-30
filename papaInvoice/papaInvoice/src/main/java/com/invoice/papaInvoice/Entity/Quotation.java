package com.invoice.papaInvoice.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationItem> items = new ArrayList<>();

    // Standard getters and setters for ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Relationship management methods for Customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Methods to manage the list of QuotationItem
    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }

    public void addItem(QuotationItem item) {
        items.add(item);
        item.setQuotation(this);
    }

    public void removeItem(QuotationItem item) {
        items.remove(item);
        item.setQuotation(null);
    }

    // Constructors, equals, hashCode, toString if needed
    // Make sure to add a default constructor for JPA
    public Quotation() {
    }

    // Constructor with Customer
    public Quotation(Customer customer) {
        this.customer = customer;
    }

    // toString method to display quotation details
    @Override
    public String toString() {
        return "Quotation{" +
                "id=" + id +
                ", customer=" + customer +
                ", items=" + items +
                '}';
    }

    // You might want to override equals and hashCode methods as well,
    // depending on whether you would like to compare Quotation objects
}
