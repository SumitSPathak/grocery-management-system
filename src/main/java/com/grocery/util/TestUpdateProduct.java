package com.grocery.util;

import java.math.BigDecimal;
import java.sql.Date;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class TestUpdateProduct {

    public static void main(String[] args) {

        try {

            Product product = new Product();

            // ID of existing product
            product.setProductId(1);

            product.setProductName("Milk");
            product.setCategory("Dairy");
            product.setPrice(new BigDecimal("75.00"));
            product.setQuantity(30);
            product.setSupplier("Amul");
            product.setExpiryDate(Date.valueOf("2026-12-31"));

            ProductDAO dao = new ProductDAOImpl();

            boolean result = dao.updateProduct(product);

            if (result) {
                System.out.println("✅ Product Updated Successfully!");
            } else {
                System.out.println("❌ Product Not Updated!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}