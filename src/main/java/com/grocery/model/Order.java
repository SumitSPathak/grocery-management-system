package com.grocery.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private int productId;
    private int supplierId;

    private String customerName;

    private int quantity;

    private BigDecimal totalAmount;

    private Timestamp orderDate;

    private String status;

    private String paymentStatus;

    private String deliveryStatus;


    // Default Constructor
    public Order() {
    }


    // Constructor for Insert
    public Order(int productId,
                 int supplierId,
                 String customerName,
                 int quantity,
                 BigDecimal totalAmount,
                 String status,
                 String paymentStatus,
                 String deliveryStatus) {

        this.productId = productId;
        this.supplierId = supplierId;
        this.customerName = customerName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
    }


    // Full Constructor

    public Order(int orderId,
                 int productId,
                 int supplierId,
                 String customerName,
                 int quantity,
                 BigDecimal totalAmount,
                 Timestamp orderDate,
                 String status,
                 String paymentStatus,
                 String deliveryStatus) {

        this.orderId = orderId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.customerName = customerName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
    }



    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }


    @Override
    public String toString() {

        return "Order{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", supplierId=" + supplierId +
                ", customerName='" + customerName + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }

}