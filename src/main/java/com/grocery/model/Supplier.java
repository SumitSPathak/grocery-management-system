package com.grocery.model;


import java.sql.Timestamp;


/**
 * Supplier Model (POJO)
 * Represents supplier table data.
 */
public class Supplier {


    private int supplierId;

    private String supplierName;

    private String companyName;

    private String phone;

    private String email;

    private String address;

    private String gstNumber;

    private Timestamp createdAt;

    private String username;

    private String password;

    private String status;




    // Default Constructor
    public Supplier() {

    }





    // Constructor without login details
    public Supplier(
            String supplierName,
            String companyName,
            String phone,
            String email,
            String address,
            String gstNumber
    ){

        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gstNumber = gstNumber;

    }





    // Constructor with username and password
    public Supplier(
            String supplierName,
            String companyName,
            String phone,
            String email,
            String address,
            String gstNumber,
            String username,
            String password
    ){

        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gstNumber = gstNumber;
        this.username = username;
        this.password = password;

    }





    // Full Constructor (Database Mapping)

    public Supplier(
            int supplierId,
            String supplierName,
            String companyName,
            String phone,
            String email,
            String address,
            String gstNumber,
            Timestamp createdAt,
            String username,
            String password,
            String status
    ){

        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gstNumber = gstNumber;
        this.createdAt = createdAt;
        this.username = username;
        this.password = password;
        this.status = status;

    }





    public int getSupplierId() {
        return supplierId;
    }


    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }




    public String getSupplierName() {
        return supplierName;
    }


    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }





    public String getCompanyName() {
        return companyName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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





    public String getGstNumber() {
        return gstNumber;
    }


    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }





    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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





    @Override
    public String toString() {

        return "Supplier{" +
                "supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gstNumber='" + gstNumber + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';

    }


}