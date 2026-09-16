package com.grocery.gui;

import javax.swing.*;
import java.awt.*;

public class CommonLoginFrame extends JFrame {

    private JComboBox<String> roleDropdown;
    private JTextField usernameField;
    private JPasswordField passwordField;


    public CommonLoginFrame() {


        setTitle("Grocery Management System - Login");

        setSize(450, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();

        panel.setLayout(null);

        panel.setBackground(
                new Color(232, 245, 233)
        );


        // =========================================
        // TITLE
        // =========================================

        JLabel title = new JLabel(
                "🛒 Grocery Login",
                SwingConstants.CENTER
        );

        title.setBounds(
                50,
                30,
                350,
                40
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        panel.add(title);


        // =========================================
        // ROLE
        // =========================================

        JLabel roleLabel =
                new JLabel("Select Role");

        roleLabel.setBounds(
                60,
                100,
                100,
                25
        );

        panel.add(roleLabel);


        String roles[] = {

                "Customer",
                "Admin",
                "Employee",
                "Supplier"

        };


        roleDropdown =
                new JComboBox<>(roles);


        roleDropdown.setBounds(
                160,
                100,
                200,
                30
        );


        panel.add(roleDropdown);


        // =========================================
        // USERNAME
        // =========================================

        JLabel userLabel =
                new JLabel("Username");


        userLabel.setBounds(
                60,
                160,
                100,
                25
        );


        panel.add(userLabel);


        usernameField =
                new JTextField();


        usernameField.setBounds(
                160,
                160,
                200,
                30
        );


        panel.add(usernameField);


        // =========================================
        // PASSWORD
        // =========================================

        JLabel passLabel =
                new JLabel("Password");


        passLabel.setBounds(
                60,
                220,
                100,
                25
        );


        panel.add(passLabel);


        passwordField =
                new JPasswordField();


        passwordField.setBounds(
                160,
                220,
                200,
                30
        );


        panel.add(passwordField);


        // =========================================
        // LOGIN BUTTON
        // =========================================

        JButton loginBtn =
                new JButton("🔐 LOGIN");


        loginBtn.setBounds(
                150,
                290,
                150,
                40
        );


        loginBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );


        panel.add(loginBtn);


        // =========================================
        // SIGN UP BUTTON
        // =========================================

        JButton signupBtn =
                new JButton(
                        "📝 New Customer? Sign Up"
                );


        signupBtn.setBounds(
                90,
                350,
                270,
                40
        );


        signupBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );


        panel.add(signupBtn);


        // =========================================
        // LOGIN ACTION
        // =========================================

        loginBtn.addActionListener(e -> {


            String role =
                    roleDropdown
                            .getSelectedItem()
                            .toString();


            String username =
                    usernameField.getText()
                            .trim();


            String password =
                    new String(
                            passwordField.getPassword()
                    );


            // Empty field validation

            if (
                    username.isEmpty()
                    ||
                    password.isEmpty()
            ) {


                JOptionPane.showMessageDialog(
                        this,
                        "Please enter username and password"
                );


                return;

            }


            // =====================================
            // ROLE BASED LOGIN
            // =====================================

            switch (role) {


                // -----------------------------
                // CUSTOMER
                // -----------------------------

                case "Customer":


                    com.grocery.model.Customer customer =
                            new com.grocery.model.Customer();


                    customer.setCustomerName(
                            username
                    );


                    new CustomerDashboard(
                            customer
                    );


                    break;


                // -----------------------------
                // ADMIN
                // -----------------------------

                case "Admin":


                    new MainDashboard();


                    break;


                // -----------------------------
                // SUPPLIER
                // -----------------------------

                case "Supplier":


                    com.grocery.model.Supplier supplier =
                            new com.grocery.model.Supplier();


                    supplier.setSupplierName(
                            username
                    );


                    new SupplierDashboard(
                            supplier
                    );


                    break;


                // -----------------------------
                // EMPLOYEE
                // -----------------------------

                case "Employee":


                    new MainDashboard();


                    break;


            }


            dispose();

        });


        // =========================================
        // SIGN UP ACTION
        // =========================================

        signupBtn.addActionListener(e -> {


            new CustomerRegisterFrame();


            dispose();

        });


        // =========================================
        // SHOW FRAME
        // =========================================

        add(panel);

        setVisible(true);

    }

}