package com.invoice.papaInvoice.Entity;


import jakarta.persistence.Embeddable;

import javax.validation.constraints.NotBlank;

@Embeddable
public class Client {

    @NotBlank(message = "Client name is required.")
    private String name;

    @NotBlank(message = "Client address is required.")
    private String address;

    @NotBlank(message = "Client GSTIN is required.")
    private String gstin;

    // Constructors
    public Client() {
    }

    public Client(String name, String address, String gstin) {
        this.name = name;
        this.address = address;
        this.gstin = gstin;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
