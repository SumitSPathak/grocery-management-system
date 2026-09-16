package com.grocery.util;

import java.math.BigDecimal;
import java.sql.Date;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class TestProduct {

    public static void main(String[] args) {

        try {

            Product product = new Product(
                    "Milk",
                    "Dairy",
                    new BigDecimal("60.00"),
                    20,
                    "Amul",
                    Date.valueOf("2026-12-31")
            );

            ProductDAO dao = new ProductDAOImpl();

            boolean result = dao.addProduct(product);

            if (result) {
                System.out.println("✅ Product Added Successfully!");
            } else {
                System.out.println("❌ Product Not Added!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}