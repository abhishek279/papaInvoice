package com.invoice.papaInvoice.exception;


public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(Long id) {
        super("Invoice not found with id: " + id);
    }

    // If you plan to search by other fields, you can overload the constructor
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
