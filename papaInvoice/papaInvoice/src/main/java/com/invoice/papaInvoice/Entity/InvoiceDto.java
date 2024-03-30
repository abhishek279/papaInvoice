package com.invoice.papaInvoice.Entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvoiceDto {
    private Long id;
    private String customerName;
    private String customerAddress;

    private BigDecimal grandTotal; // New field for grand total


    private List<ProductItemDto> productItems;
    private DispatchDetailDto dispatchDetail;

    // Constructor
    public InvoiceDto() {}

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }



    public List<ProductItemDto> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItemDto> productItems) {
        this.productItems = productItems;
    }

    public DispatchDetailDto getDispatchDetail() {
        return dispatchDetail;
    }

    public void setDispatchDetail(DispatchDetailDto dispatchDetail) {
        this.dispatchDetail = dispatchDetail;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
// Nested DTO for Product Items


    // Nested DTO for Dispatch Details
    public static class DispatchDetailDto {
        private String challanNo;
        private LocalDate challanDate;
        private String place;
        private String poNo;
        private String size;
        private int boxNumber;
        private double dispatchQty;
        private String transport;
        private String lrNo;
        private LocalDate lrDate;
        private BigDecimal freight;
        private String deliveryType;

        // Constructors, getters, and setters

        public String getChallanNo() {
            return challanNo;
        }

        public void setChallanNo(String challanNo) {
            this.challanNo = challanNo;
        }

        public LocalDate getChallanDate() {
            return challanDate;
        }

        public void setChallanDate(LocalDate challanDate) {
            this.challanDate = challanDate;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPoNo() {
            return poNo;
        }

        public void setPoNo(String poNo) {
            this.poNo = poNo;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getBoxNumber() {
            return boxNumber;
        }

        public void setBoxNumber(int boxNumber) {
            this.boxNumber = boxNumber;
        }

        public double getDispatchQty() {
            return dispatchQty;
        }

        public void setDispatchQty(double dispatchQty) {
            this.dispatchQty = dispatchQty;
        }

        public String getTransport() {
            return transport;
        }

        public void setTransport(String transport) {
            this.transport = transport;
        }

        public String getLrNo() {
            return lrNo;
        }

        public void setLrNo(String lrNo) {
            this.lrNo = lrNo;
        }

        public LocalDate getLrDate() {
            return lrDate;
        }

        public void setLrDate(LocalDate lrDate) {
            this.lrDate = lrDate;
        }

        public BigDecimal getFreight() {
            return freight;
        }

        public void setFreight(BigDecimal freight) {
            this.freight = freight;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }
    }
}