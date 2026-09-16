package com.grocery.util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {

                System.out.println("Database connected successfully!");
                System.out.println("Connected to : " + connection.getMetaData().getURL());

            } else {

                System.out.println("Connection Failed!");

            }

        } catch (SQLException e) {

            System.out.println("Connection Failed!");
            e.printStackTrace();

        }

    }

}