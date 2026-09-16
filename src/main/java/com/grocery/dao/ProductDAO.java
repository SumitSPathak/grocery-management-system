package com.grocery.dao;

import java.sql.SQLException;
import java.util.List;

import com.grocery.model.Product;


public interface ProductDAO {


    boolean addProduct(Product product) throws SQLException;


    boolean updateProduct(Product product) throws SQLException;


    boolean deleteProduct(int productId) throws SQLException;


    Product getProductById(int productId) throws SQLException;


    List<Product> getAllProducts() throws SQLException;



    // Pagination
    List<Product> getProductsByLimit(
            int limit,
            int offset
    ) throws SQLException;



    // Price Filter
    List<Product> getProductsByPrice(
            double price
    ) throws SQLException;



    // Category Filter
    List<Product> getProductsByCategory(
            String category
    ) throws SQLException;



    int getTotalProducts() throws SQLException;


    int getTotalCategories() throws SQLException;


    int getLowStockCount() throws SQLException;


    int getOutOfStockCount() throws SQLException;


    int getExpiredProductsCount() throws SQLException;



    List<Product> getProductsByStatus(
            String status
    ) throws SQLException;



    List<Product> searchProducts(
            String keyword
    ) throws SQLException;



    boolean updateStock(
            int productId,
            int quantity
    ) throws SQLException;

}