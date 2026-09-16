package com.grocery.model;

import java.sql.Timestamp;

public class Customer {


    private int customerId;

    private String customerName;

    private String phone;

    private String email;

    private String address;

    private String username;

    private String password;

    private String status;

    private Timestamp createdAt;



    // Default Constructor
    public Customer() {

    }



    // Constructor without ID
    public Customer(
            String customerName,
            String phone,
            String email,
            String address,
            String username,
            String password
    ){

        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.username = username;
        this.password = password;

    }



    // Full Constructor

    public Customer(
            int customerId,
            String customerName,
            String phone,
            String email,
            String address,
            String username,
            String password,
            String status,
            Timestamp createdAt
    ){

        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.username = username;
        this.password = password;
        this.status = status;
        this.createdAt = createdAt;

    }



    // Getter Setter


    public int getCustomerId() {
        return customerId;
    }


    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }



    public String getCustomerName() {
        return customerName;
    }


    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }



    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }



    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }



    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }



    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }



    @Override
    public String toString() {

        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';

    }

}