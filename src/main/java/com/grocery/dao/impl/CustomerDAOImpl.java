package com.grocery.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grocery.dao.CustomerDAO;
import com.grocery.model.Customer;
import com.grocery.util.DBConnection;

public class CustomerDAOImpl implements CustomerDAO {

    // =========================================================
    // ADD CUSTOMER
    // =========================================================
    @Override
    public boolean addCustomer(Customer customer) throws SQLException {

        String sql =
                "INSERT INTO customer " +
                "(customer_name, phone, email, address, username, password, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getUsername());
            ps.setString(6, customer.getPassword());
            ps.setString(7, "ACTIVE");

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================
    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {

        String sql =
                "UPDATE customer SET " +
                "customer_name=?, phone=?, email=?, address=? " +
                "WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setInt(5, customer.getCustomerId());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // DELETE CUSTOMER
    // =========================================================
    @Override
    public boolean deleteCustomer(int customerId) throws SQLException {

        String sql =
                "DELETE FROM customer WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================
    @Override
    public Customer getCustomerById(int customerId) throws SQLException {

        String sql =
                "SELECT * FROM customer WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        }

        return null;
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================
    @Override
    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> list = new ArrayList<>();

        String sql =
                "SELECT * FROM customer " +
                "ORDER BY customer_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCustomer(rs));
            }
        }

        return list;
    }

    // =========================================================
    // CUSTOMER LOGIN
    // =========================================================
    @Override
    public Customer login(String username, String password)
            throws SQLException {

        String sql =
                "SELECT * FROM customer " +
                "WHERE username=? AND password=? AND status='ACTIVE'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapCustomer(rs);
                }
            }
        }

        return null;
    }

    // =========================================================
    // TOTAL CUSTOMERS
    // =========================================================
    @Override
    public int getTotalCustomers() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM customer";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // =========================================================
    // MAP RESULTSET TO CUSTOMER OBJECT
    // =========================================================
    private Customer mapCustomer(ResultSet rs) throws SQLException {

        Customer customer = new Customer();

        customer.setCustomerId(
                rs.getInt("customer_id")
        );

        customer.setCustomerName(
                rs.getString("customer_name")
        );

        customer.setPhone(
                rs.getString("phone")
        );

        customer.setEmail(
                rs.getString("email")
        );

        customer.setAddress(
                rs.getString("address")
        );

        customer.setUsername(
                rs.getString("username")
        );

        customer.setPassword(
                rs.getString("password")
        );

        customer.setStatus(
                rs.getString("status")
        );

        customer.setCreatedAt(
                rs.getTimestamp("created_at")
        );

        return customer;
    }
}