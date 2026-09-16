package com.grocery.util;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;

public class TestDeleteProduct {

    public static void main(String[] args) {

        try {

            ProductDAO dao = new ProductDAOImpl();

            boolean result = dao.deleteProduct(1);

            if (result) {
                System.out.println("☑ Product Deleted Successfully!");
            } else {
                System.out.println("✖ Product Not Found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}