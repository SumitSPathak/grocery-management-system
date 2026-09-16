package com.grocery.gui;

import com.grocery.dao.SupplierDAO;
import com.grocery.dao.impl.SupplierDAOImpl;
import com.grocery.model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * AddSupplierFrame.java
 *
 * Professional Java Swing screen for adding a new supplier record.
 * Follows the same green-themed, full-screen layout used across the
 * rest of the application (e.g. SupplierManagementFrame,
 * AddProductFrame) so the module feels like a natural part of the
 * same system.
 *
 * Uses the existing SupplierDAO / SupplierDAOImpl / Supplier classes
 * exactly as they are defined in the project — no new methods or
 * fields are assumed.
 */
public class AddSupplierFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with SupplierManagementFrame)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_CARD_BG      = Color.WHITE;
    private static final Color COLOR_CARD_BORDER  = new Color(200, 230, 201);

    private static final Color COLOR_SAVE_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_SAVE_HOVER  = new Color(27, 94, 32);   // Darker green on hover
    private static final Color COLOR_CLEAR_BG    = new Color(97, 97, 97);   // Neutral gray
    private static final Color COLOR_CLEAR_HOVER = new Color(66, 66, 66);
    private static final Color COLOR_BACK_BG     = new Color(198, 40, 40);  // Red accent
    private static final Color COLOR_BACK_HOVER  = new Color(140, 20, 20);
    private static final Color COLOR_BUTTON_TEXT = Color.WHITE;

    private static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_FIELD   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 16);

    // Simple, practical email pattern: something@something.something
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // DAO instance used to persist the new supplier
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();

    private JTextField supplierNameField;
    private JTextField companyNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea addressArea;
    private JTextField gstNumberField;
    private JTextField passwordField;
    private JTextField confirmPasswordField;
    private JTextField usernameField;
    private JButton saveButton;
    private JButton clearButton;
    private JButton backButton;

    /**
     * Constructs and displays the Add Supplier screen.
     */
    public AddSupplierFrame() {
        initializeFrame();
        buildUI();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, full-screen
     * behaviour, close operation, and default look.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - Add Supplier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BODY_BG);
    }

    /**
     * Builds the complete UI: header banner, form card, and footer.
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the top header banner showing the screen title.
     *
     * @return the fully configured header JPanel
     */
    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel("Add Supplier", SwingConstants.CENTER);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_HEADER_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Enter the supplier's details below to add them to the system",
                SwingConstants.CENTER);
        subtitleLabel.setFont(FONT_SUBTEXT);
        subtitleLabel.setForeground(COLOR_HEADER_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    /**
     * Builds the central content area containing the form card,
     * centered both horizontally and vertically.
     *
     * @return the fully configured center JPanel
     */
    private JPanel buildCenterPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(COLOR_BODY_BG);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(35, 45, 30, 45)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Supplier Name
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Supplier Name:"), gbc);
        supplierNameField = new JTextField(20);
        supplierNameField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(supplierNameField, gbc);
        row++;

        // Company Name
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Company Name:"), gbc);
        companyNameField = new JTextField(20);
        companyNameField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(companyNameField, gbc);
        row++;

        // Phone
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        phoneField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(phoneField, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Email:"), gbc);
        emailField = new JTextField(20);
        emailField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(emailField, gbc);
        row++;

        // Address (JTextArea inside JScrollPane)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        cardPanel.add(createFieldLabel("Address:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;

        addressArea = new JTextArea(4, 20);
        addressArea.setFont(FONT_FIELD);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(addressScrollPane, gbc);
        row++;

        // GST Number
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("GST Number:"), gbc);
        gstNumberField = new JTextField(20);
        gstNumberField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(gstNumberField, gbc);
        row++;
     // Username

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        cardPanel.add(
                createFieldLabel("Username:"),
                gbc
        );


        usernameField = new JTextField(20);

        usernameField.setFont(
                FONT_FIELD
        );


        gbc.gridx = 1;
        gbc.weightx = 0.65;


        cardPanel.add(
                usernameField,
                gbc
        );


        row++;


        // Password

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        cardPanel.add(
                createFieldLabel("Password:"),
                gbc
        );


        passwordField = new JTextField(20);

        passwordField.setFont(
                FONT_FIELD
        );


        gbc.gridx = 1;
        gbc.weightx = 0.65;


        cardPanel.add(
                passwordField,
                gbc
        );


        row++;
     // Confirm Password

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        cardPanel.add(
                createFieldLabel("Confirm Password:"),
                gbc
        );


        confirmPasswordField = new JTextField(20);

        confirmPasswordField.setFont(
                FONT_FIELD
        );


        gbc.gridx = 1;
        gbc.weightx = 0.65;


        cardPanel.add(
                confirmPasswordField,
                gbc
        );


        row++;
        // Button row: Save, Clear, Back
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(COLOR_CARD_BG);

        saveButton = createStyledButton("Save", COLOR_SAVE_BG, COLOR_SAVE_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSave();
                    }
                });

        clearButton = createStyledButton("Clear", COLOR_CLEAR_BG, COLOR_CLEAR_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clearFields();
                    }
                });

        backButton = createStyledButton("Back", COLOR_BACK_BG, COLOR_BACK_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new SupplierManagementFrame();
                    }
                });

        buttonRow.add(saveButton);
        buttonRow.add(clearButton);
        buttonRow.add(backButton);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 5, 8);
        cardPanel.add(buttonRow, gbc);

        wrapperPanel.add(cardPanel);
        return wrapperPanel;
    }

    /**
     * Builds a simple footer bar shown at the bottom of the screen.
     *
     * @return the fully configured footer JPanel
     */
    private JPanel buildFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(COLOR_HEADER_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel footerLabel = new JLabel("Grocery Management System \u00A9 Supplier Module");
        footerLabel.setForeground(COLOR_HEADER_TEXT);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    // ---------------------------------------------------------
    // Helper methods: field label + styled button creation
    // ---------------------------------------------------------

    /**
     * Creates a consistently styled label for a form field.
     *
     * @param text the label text
     * @return a fully configured JLabel
     */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        return label;
    }

    /**
     * Creates a professionally styled button, complete with hover
     * feedback and a click handler.
     *
     * @param text           the button label
     * @param baseColor      the button's normal background color
     * @param hoverColor     the button's background color on mouse hover
     * @param actionListener the action to run when the button is clicked
     * @return a fully configured JButton
     */
    private JButton createStyledButton(String text, Color baseColor, Color hoverColor,
                                        ActionListener actionListener) {

        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(COLOR_BUTTON_TEXT);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(140, 42));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // ---------------------------------------------------------
    // Business logic: validation, save, clear
    // ---------------------------------------------------------

    /**
     * Handles the Save button click:
     * 1. Validates all fields.
     * 2. Builds a Supplier object from the form values.
     * 3. Persists it via SupplierDAOImpl.addSupplier().
     * 4. Shows a success or error message.
     */
    private void handleSave() {
        String supplierName = supplierNameField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();
        String gstNumber = gstNumberField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();


        if(!password.equals(confirmPassword)){

            showValidationError("Password and Confirm Password do not match.");

            confirmPasswordField.requestFocus();

            return;
        }
        // ---- Supplier Name: required ----
        if (supplierName.isEmpty()) {
            showValidationError("Supplier Name cannot be empty.");
            supplierNameField.requestFocus();
            return;
        }

        // ---- Phone: required, exactly 10 digits ----
        if (phone.isEmpty()) {
            showValidationError("Phone number is required.");
            phoneField.requestFocus();
            return;
        }
        if (!phone.matches("\\d{10}")) {
            showValidationError("Phone number must contain exactly 10 digits.");
            phoneField.requestFocus();
            return;
        }

        // ---- Email: required, valid format ----
        if (email.isEmpty()) {
            showValidationError("Email is required.");
            emailField.requestFocus();
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showValidationError("Please enter a valid email address.");
            emailField.requestFocus();
            return;
        }

        // ---- GST Number: required ----
        if (gstNumber.isEmpty()) {
            showValidationError("GST Number is required.");
            gstNumberField.requestFocus();
            return;
        }

        // Build the Supplier object using the constructor that matches
        // a new, not-yet-persisted supplier (no supplierId/createdAt).
        Supplier supplier = new Supplier(
                supplierName,
                companyName,
                phone,
                email,
                address,
                gstNumber,
                username,
                password
        );
        try {
            boolean isSaved = supplierDAO.addSupplier(supplier);

            if (isSaved) {
                JOptionPane.showMessageDialog(this,
                        "Supplier added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add supplier. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while saving supplier: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows a validation warning dialog with the given message.
     *
     * @param message the validation error to display
     */
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this,
                message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Clears all form fields and returns focus to the first field.
     */
    private void clearFields() {
        supplierNameField.setText("");
        companyNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressArea.setText("");
        gstNumberField.setText("");
        supplierNameField.requestFocus();
    }
}