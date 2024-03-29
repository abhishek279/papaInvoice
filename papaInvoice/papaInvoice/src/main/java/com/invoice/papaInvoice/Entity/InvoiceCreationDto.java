package com.invoice.papaInvoice.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceCreationDto {
    private Long customerId;
    private String productName;
    private String description;
    private int quantity;
    private double rate;
    private BigDecimal totalAmount;

    private double gstRate;

    // Nested class for dispatch details
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

        // Getters and setters
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

    private DispatchDetailDto dispatchDetail;

    // Getters and setters for InvoiceCreationDto fields


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    // Getters and setters for dispatchDetail
    public DispatchDetailDto getDispatchDetail() {
        return dispatchDetail;
    }

    public void setDispatchDetail(DispatchDetailDto dispatchDetail) {
        this.dispatchDetail = dispatchDetail;
    }
}
