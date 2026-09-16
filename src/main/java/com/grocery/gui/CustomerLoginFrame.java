package com.grocery.gui;

import javax.swing.*;
import java.awt.*;

import com.grocery.dao.CustomerDAO;
import com.grocery.dao.impl.CustomerDAOImpl;
import com.grocery.model.Customer;

public class CustomerLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginBtn;
    private JButton signupBtn;
    private JButton backBtn;

    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    // =========================
    // COLORS
    // =========================
    private static final Color DARK_GREEN =
            new Color(27, 94, 32);

    private static final Color LIGHT_GREEN =
            new Color(232, 245, 233);

    // =========================
    // CONSTRUCTOR
    // =========================
    public CustomerLoginFrame() {

        initFrame();
        initUI();

        setVisible(true);
    }

    // =========================
    // FRAME SETTINGS
    // =========================
    private void initFrame() {

        setTitle("Customer Login");

        setSize(500, 450);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
    }

    // =========================
    // UI
    // =========================
    private void initUI() {

        // -------------------------
        // HEADER
        // -------------------------
        JPanel header = new JPanel();

        header.setBackground(DARK_GREEN);

        JLabel title =
                new JLabel("🛒 Customer Login");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(Color.WHITE);

        header.add(title);

        add(header, BorderLayout.NORTH);

        // -------------------------
        // MAIN PANEL
        // -------------------------
        JPanel panel =
                new JPanel(new GridBagLayout());

        panel.setBackground(LIGHT_GREEN);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(15, 15, 15, 15);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // -------------------------
        // USERNAME LABEL
        // -------------------------
        JLabel userLabel =
                new JLabel("Username");

        userLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(userLabel, gbc);

        // -------------------------
        // USERNAME FIELD
        // -------------------------
        usernameField =
                new JTextField(18);

        gbc.gridx = 1;

        panel.add(usernameField, gbc);

        // -------------------------
        // PASSWORD LABEL
        // -------------------------
        JLabel passLabel =
                new JLabel("Password");

        passLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(passLabel, gbc);

        // -------------------------
        // PASSWORD FIELD
        // -------------------------
        passwordField =
                new JPasswordField(18);

        gbc.gridx = 1;

        panel.add(passwordField, gbc);

        // -------------------------
        // BUTTONS
        // -------------------------
        loginBtn =
                new JButton("Sign In");

        signupBtn =
                new JButton("Sign Up");

        backBtn =
                new JButton("Back");

        // -------------------------
        // BUTTON PANEL
        // -------------------------
        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(LIGHT_GREEN);

        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        buttonPanel.add(backBtn);

        // -------------------------
        // ADD PANELS
        // -------------------------
        add(panel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        // -------------------------
        // LOGIN BUTTON
        // -------------------------
        loginBtn.addActionListener(
                e -> loginCustomer()
        );

        // -------------------------
        // SIGN UP BUTTON
        // -------------------------
        signupBtn.addActionListener(e -> {

            dispose();

            new CustomerRegisterFrame();

        });

        // -------------------------
        // BACK BUTTON
        // -------------------------
        backBtn.addActionListener(e -> {

            dispose();

            new LoginSelectionFrame();

        });
    }

    // =========================
    // CUSTOMER LOGIN
    // =========================
    private void loginCustomer() {

        try {

            String username =
                    usernameField
                            .getText()
                            .trim();

            String password =
                    new String(
                            passwordField
                                    .getPassword()
                    ).trim();

            // -------------------------
            // VALIDATION
            // -------------------------
            if (username.isEmpty()
                    || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username and Password are mandatory",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // -------------------------
            // DATABASE LOGIN
            // -------------------------
            Customer customer =
                    customerDAO.login(
                            username,
                            password
                    );

            // -------------------------
            // SUCCESS
            // -------------------------
            if (customer != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new CustomerDashboard(customer);

            }

            // -------------------------
            // INVALID LOGIN
            // -------------------------
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        }

        // -------------------------
        // ERROR HANDLING
        // -------------------------
        catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to login.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
