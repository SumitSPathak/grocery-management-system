package com.grocery.dao;

import java.sql.SQLException;
import java.util.List;

import com.grocery.model.Customer;


public interface CustomerDAO {


    boolean addCustomer(Customer customer) throws SQLException;


    boolean updateCustomer(Customer customer) throws SQLException;


    boolean deleteCustomer(int customerId) throws SQLException;


    Customer getCustomerById(int customerId) throws SQLException;


    List<Customer> getAllCustomers() throws SQLException;


    Customer login(String username, String password) throws SQLException;


    int getTotalCustomers() throws SQLException;


}