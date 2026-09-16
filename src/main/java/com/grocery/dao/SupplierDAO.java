package com.grocery.dao;

import java.sql.SQLException;
import java.util.List;

import com.grocery.model.Supplier;

/**
 * SupplierDAO
 * -----------
 * Interface defining all database operations for the Supplier module.
 * The implementation of these methods is provided in SupplierDAOImpl.
 */
public interface SupplierDAO {

    /**
     * Add a new supplier.
     */
    boolean addSupplier(Supplier supplier) throws SQLException;

    /**
     * Update an existing supplier.
     */
    boolean updateSupplier(Supplier supplier) throws SQLException;

    /**
     * Delete supplier by ID.
     */
    boolean deleteSupplier(int supplierId) throws SQLException;

    /**
     * Get supplier by ID.
     */
    Supplier getSupplierById(int supplierId) throws SQLException;

    /**
     * Get all suppliers.
     */
    List<Supplier> getAllSuppliers() throws SQLException;

    /**
     * Get total supplier count.
     */
    int getTotalSuppliers() throws SQLException;

    Supplier login(String username, String password) throws SQLException;
}