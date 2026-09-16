package com.grocery.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Product {

    private int productId;

    private String productName;

    private String category;

    private BigDecimal price;

    private int quantity;

    private String supplier;

    private Date expiryDate;

    private Timestamp createdAt;


    // ---------- Constructors ----------

    public Product() {
    }


    public Product(String productName, String category, BigDecimal price,
                   int quantity, String supplier, Date expiryDate) {

        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
    }


    public Product(int productId, String productName, String category,
                   BigDecimal price, int quantity,
                   String supplier, Date expiryDate,
                   Timestamp createdAt) {

        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }



    // ---------- Getters and Setters ----------


    public int getProductId() {
        return productId;
    }


    public void setProductId(int productId) {
        this.productId = productId;
    }



    public String getProductName() {
        return productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }



    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }



    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public String getSupplier() {
        return supplier;
    }


    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }



    public Date getExpiryDate() {
        return expiryDate;
    }


    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }



    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }



    @Override
    public String toString() {

        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", supplier='" + supplier + '\'' +
                ", expiryDate=" + expiryDate +
                ", createdAt=" + createdAt +
                '}';
    }

}