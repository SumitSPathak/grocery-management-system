package com.grocery.gui;

import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;
import com.grocery.model.Admin;

import javax.swing.*;
import java.awt.*;

/**
 * AddAdminFrame
 * -------------
 * Screen for adding a new admin account, opened from AdminManagementFrame.
 *
 * Save button is now connected to MySQL via AdminDAO — it checks for an
 * existing username before inserting a new admin record.
 */
public class AddAdminFrame extends JFrame {

    // Same theme palette as the rest of the application
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);

    // DAO instance used to check for existing usernames and insert new admins
    private final AdminDAO adminDAO = new AdminDAOImpl();

    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    private JButton saveBtn;
    private JButton resetBtn;
    private JButton backBtn;

    public AddAdminFrame() {
        initFrame();
        initHeader();
        initFormPanel();
        initButtonPanel();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {

        setTitle("Add a New Admin ");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(true);

        setLayout(new BorderLayout());

        getContentPane().setBackground(LIGHT_GREEN);
    }    /**
     * Top header showing the screen title.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("➕  Add Admin");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center panel with the form fields.
     */
    private void initFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(LIGHT_GREEN);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        formPanel.add(fullNameLabel, gbc);

        fullNameField = new JTextField();
        fullNameField.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(fullNameField, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.35;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.35;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(passwordField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.35;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(fieldFont);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(emailField, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Save, Reset, and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 25, 60));

        saveBtn = createStyledButton("💾  Save", BUTTON_GREEN);
        resetBtn = createStyledButton("Reset", RESET_GRAY);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        saveBtn.addActionListener(e -> handleSave());
        resetBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the Save button click:
     * 1. Validates all fields are filled.
     * 2. Checks if the username already exists in the database.
     * 3. If not, builds an Admin object and inserts it via AdminDAO.
     */
    private void handleSave() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        // ---- Basic field validation ----
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- Check for duplicate username before inserting ----
        Admin existingAdmin = adminDAO.getAdminByUsername(username);
        if (existingAdmin != null) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose a different username.",
                    "Duplicate Username", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ---- Build Admin object and insert into database ----
        Admin newAdmin = new Admin();
        newAdmin.setFullName(fullName);
        newAdmin.setUsername(username);
        newAdmin.setPassword(password);
        newAdmin.setEmail(email);

        boolean isSaved = adminDAO.addAdmin(newAdmin);

        if (isSaved) {
            JOptionPane.showMessageDialog(this,
                    "Admin Added Successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add admin. Please try again.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all form fields.
     */
    private void resetForm() {
        fullNameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        fullNameField.requestFocus();
    }

    /**
     * Helper to create a consistently styled button with a custom background color.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}