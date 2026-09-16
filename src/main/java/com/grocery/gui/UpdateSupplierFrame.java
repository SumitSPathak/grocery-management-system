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
 * UpdateSupplierFrame.java
 *
 * Professional Java Swing screen for updating an existing supplier
 * record. Follows the same green-themed, full-screen layout used
 * across the rest of the application (e.g. SupplierManagementFrame,
 * AddSupplierFrame, SearchSupplierFrame).
 *
 * Flow:
 *   1. User enters Supplier ID and clicks Search — fields populate from DB.
 *   2. User edits field(s) and clicks Update to save changes.
 *
 * Uses the existing SupplierDAO / SupplierDAOImpl / Supplier classes
 * exactly as they are defined in the project — no new methods or
 * fields are assumed.
 */
public class UpdateSupplierFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with the rest of the Supplier module)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_CARD_BG      = Color.WHITE;
    private static final Color COLOR_CARD_BORDER  = new Color(200, 230, 201);

    private static final Color COLOR_SEARCH_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_SEARCH_HOVER = new Color(27, 94, 32);    // Darker green on hover
    private static final Color COLOR_UPDATE_BG    = new Color(46, 125, 50);
    private static final Color COLOR_UPDATE_HOVER = new Color(27, 94, 32);
    private static final Color COLOR_BACK_BG      = new Color(198, 40, 40);   // Red accent
    private static final Color COLOR_BACK_HOVER   = new Color(140, 20, 20);
    private static final Color COLOR_BUTTON_TEXT  = Color.WHITE;

    private static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_FIELD   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 16);

    // Simple, practical email pattern: something@something.something
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // DAO instance used for search and update operations
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();

    private JTextField supplierIdField;
    private JButton searchButton;

    private JTextField supplierNameField;
    private JTextField companyNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea addressArea;
    private JTextField gstNumberField;

    private JButton updateButton;
    private JButton backButton;

    /**
     * Constructs and displays the Update Supplier screen.
     */
    public UpdateSupplierFrame() {
        initializeFrame();
        buildUI();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, full-screen
     * behaviour, close operation, and default look.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - Update Supplier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BODY_BG);
    }

    /**
     * Builds the complete UI: header banner, search + edit card, and footer.
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

        JLabel titleLabel = new JLabel("Update Supplier", SwingConstants.CENTER);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_HEADER_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Search a supplier by ID, edit the details, and save changes",
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
     * Builds the central content area containing the search row and
     * editable fields, centered in a card.
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

        // Card heading
        JLabel cardHeading = new JLabel("Search & Update Supplier");
        cardHeading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardHeading.setForeground(COLOR_HEADER_BG);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 20, 8);
        cardPanel.add(cardHeading, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 8, 10, 8);
        row++;

        // Supplier ID label + field
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Supplier ID:"), gbc);
        supplierIdField = new JTextField(20);
        supplierIdField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(supplierIdField, gbc);
        row++;

        // Search button
        searchButton = createStyledButton("Search", COLOR_SEARCH_BG, COLOR_SEARCH_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSearch();
                    }
                });
        gbc.gridx = 1; gbc.gridy = row;
        gbc.insets = new Insets(5, 8, 20, 8);
        cardPanel.add(searchButton, gbc);
        gbc.insets = new Insets(10, 8, 10, 8);
        row++;

        // Separator before editable fields
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        cardPanel.add(separator, gbc);
        gbc.gridwidth = 1;
        row++;

        // ---- Editable fields ----
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Supplier Name:"), gbc);
        supplierNameField = new JTextField(20);
        supplierNameField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(supplierNameField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Company Name:"), gbc);
        companyNameField = new JTextField(20);
        companyNameField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(companyNameField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        phoneField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(phoneField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Email:"), gbc);
        emailField = new JTextField(20);
        emailField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(emailField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        cardPanel.add(createFieldLabel("Address:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;

        addressArea = new JTextArea(3, 20);
        addressArea.setFont(FONT_FIELD);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(addressScrollPane, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("GST Number:"), gbc);
        gstNumberField = new JTextField(20);
        gstNumberField.setFont(FONT_FIELD);
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(gstNumberField, gbc);
        row++;

        // Button row: Update, Back
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(COLOR_CARD_BG);

        updateButton = createStyledButton("Update", COLOR_UPDATE_BG, COLOR_UPDATE_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleUpdate();
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

        buttonRow.add(updateButton);
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
    // Helper methods: labels, styled buttons
    // ---------------------------------------------------------

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        return label;
    }

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
    // Business logic: search + update
    // ---------------------------------------------------------

    /**
     * Handles the Search button click:
     * 1. Reads and validates the Supplier ID.
     * 2. Fetches the supplier via SupplierDAO.getSupplierById().
     * 3. Populates the editable fields on success, or shows an error
     *    message if not found or if input is invalid.
     */
    private void handleSearch() {
        String idText = supplierIdField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Supplier ID.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId;
        try {
            supplierId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Supplier ID must be a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Supplier supplier = supplierDAO.getSupplierById(supplierId);

            if (supplier == null) {
                JOptionPane.showMessageDialog(this,
                        "Supplier not found.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                clearEditableFields();
                return;
            }

            supplierNameField.setText(supplier.getSupplierName());
            companyNameField.setText(supplier.getCompanyName());
            phoneField.setText(supplier.getPhone());
            emailField.setText(supplier.getEmail());
            addressArea.setText(supplier.getAddress());
            gstNumberField.setText(supplier.getGstNumber());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while searching: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the Update button click:
     * 1. Validates the Supplier ID and all editable fields.
     * 2. Builds a Supplier object with the updated values.
     * 3. Persists it via SupplierDAO.updateSupplier().
     * 4. Shows a success or error message.
     */
    private void handleUpdate() {
        String idText = supplierIdField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please search for a supplier first (enter Supplier ID and click Search).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId;
        try {
            supplierId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Supplier ID must be a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String supplierName = supplierNameField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();
        String gstNumber = gstNumberField.getText().trim();

        // ---- Supplier Name: required ----
        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Supplier Name cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            supplierNameField.requestFocus();
            return;
        }

        // ---- Phone: required, exactly 10 digits ----
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Phone number is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Phone number must contain exactly 10 digits.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return;
        }

        // ---- Email: required, valid format ----
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        // ---- GST Number: required ----
        if (gstNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "GST Number is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            gstNumberField.requestFocus();
            return;
        }

        // Build the Supplier object using the no-arg constructor + setters,
        // since we need to set supplierId (which the 6-arg constructor
        // does not accept).
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        supplier.setSupplierName(supplierName);
        supplier.setCompanyName(companyName);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        supplier.setGstNumber(gstNumber);

        try {
            boolean isUpdated = supplierDAO.updateSupplier(supplier);

            if (isUpdated) {
                JOptionPane.showMessageDialog(this,
                        "Supplier updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Update failed. Please check the Supplier ID and try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while updating: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all editable fields (used when a search finds no matching supplier).
     */
    private void clearEditableFields() {
        supplierNameField.setText("");
        companyNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressArea.setText("");
        gstNumberField.setText("");
    }
}