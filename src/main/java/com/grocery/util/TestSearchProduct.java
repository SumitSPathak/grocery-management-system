package com.grocery.util;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class TestSearchProduct {

    public static void main(String[] args) {

        try {

            ProductDAO dao = new ProductDAOImpl();

            Product product = dao.getProductById(1);

            if (product != null) {

                System.out.println("===== PRODUCT FOUND =====");
                System.out.println(product);

            } else {

                System.out.println("Product Not Found!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}