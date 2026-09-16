package com.grocery.util;

import java.util.List;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class TestViewProducts {

    public static void main(String[] args) {

        try {

            ProductDAO dao = new ProductDAOImpl();

            List<Product> products = dao.getAllProducts();

            if (products.isEmpty()) {

                System.out.println("No Products Found.");

            } else {

                System.out.println("===== PRODUCT LIST =====");

                for (Product product : products) {
                    System.out.println(product);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}