package com.grocery.gui;

import com.grocery.dao.SupplierDAO;
import com.grocery.dao.impl.SupplierDAOImpl;
import com.grocery.model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * DeleteSupplierFrame.java
 *
 * Professional Java Swing screen for deleting an existing supplier
 * record. Follows the same green-themed, full-screen layout used
 * across the rest of the application (e.g. SupplierManagementFrame,
 * AddSupplierFrame, SearchSupplierFrame, UpdateSupplierFrame).
 *
 * Flow:
 *   1. User enters Supplier ID and clicks Search — read-only fields
 *      populate from DB so the user can confirm they're deleting the
 *      right supplier.
 *   2. User clicks Delete — a confirmation dialog appears before the
 *      record is actually removed.
 *
 * Uses the existing SupplierDAO / SupplierDAOImpl / Supplier classes
 * exactly as they are defined in the project — no new methods or
 * fields are assumed.
 */
public class DeleteSupplierFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with the rest of the Supplier module)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_CARD_BG      = Color.WHITE;
    private static final Color COLOR_CARD_BORDER  = new Color(200, 230, 201);
    private static final Color COLOR_READONLY_BG  = new Color(245, 245, 245);

    private static final Color COLOR_SEARCH_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_SEARCH_HOVER = new Color(27, 94, 32);    // Darker green on hover
    private static final Color COLOR_DELETE_BG    = new Color(198, 40, 40);   // Red - delete action
    private static final Color COLOR_DELETE_HOVER = new Color(140, 20, 20);
    private static final Color COLOR_BACK_BG      = new Color(97, 97, 97);    // Neutral gray for Back
    private static final Color COLOR_BACK_HOVER   = new Color(66, 66, 66);
    private static final Color COLOR_BUTTON_TEXT  = Color.WHITE;

    private static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_FIELD   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 16);

    // DAO instance used for lookup and delete operations
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();

    private JTextField supplierIdField;
    private JButton searchButton;

    private JTextField supplierNameValue;
    private JTextField companyNameValue;
    private JTextField phoneValue;
    private JTextField emailValue;
    private JTextArea addressValue;
    private JTextField gstNumberValue;

    private JButton deleteButton;
    private JButton backButton;

    // Tracks the ID of the currently loaded supplier so Delete always
    // acts on the record that was actually searched and displayed.
    private Integer loadedSupplierId = null;

    /**
     * Constructs and displays the Delete Supplier screen.
     */
    public DeleteSupplierFrame() {
        initializeFrame();
        buildUI();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, full-screen
     * behaviour, close operation, and default look.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - Delete Supplier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BODY_BG);
    }

    /**
     * Builds the complete UI: header banner, search + confirm card, and footer.
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

        JLabel titleLabel = new JLabel("Delete Supplier", SwingConstants.CENTER);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_HEADER_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Search a supplier by ID, confirm the details, then delete the record",
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
     * read-only confirmation fields, centered in a card.
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
        JLabel cardHeading = new JLabel("Search Supplier to Delete");
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

        // Separator before result fields
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        cardPanel.add(separator, gbc);
        gbc.gridwidth = 1;
        row++;

        // ---- Read-only result fields (for confirming before delete) ----
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Supplier Name:"), gbc);
        supplierNameValue = createReadOnlyField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(supplierNameValue, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Company Name:"), gbc);
        companyNameValue = createReadOnlyField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(companyNameValue, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Phone:"), gbc);
        phoneValue = createReadOnlyField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(phoneValue, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("Email:"), gbc);
        emailValue = createReadOnlyField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(emailValue, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        cardPanel.add(createFieldLabel("Address:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;

        addressValue = new JTextArea(3, 20);
        addressValue.setFont(FONT_FIELD);
        addressValue.setLineWrap(true);
        addressValue.setWrapStyleWord(true);
        addressValue.setEditable(false);
        addressValue.setBackground(COLOR_READONLY_BG);
        JScrollPane addressScrollPane = new JScrollPane(addressValue);
        addressScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(addressScrollPane, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        cardPanel.add(createFieldLabel("GST Number:"), gbc);
        gstNumberValue = createReadOnlyField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        cardPanel.add(gstNumberValue, gbc);
        row++;

        // Button row: Delete, Back
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(COLOR_CARD_BG);

        deleteButton = createStyledButton("Delete", COLOR_DELETE_BG, COLOR_DELETE_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleDelete();
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

        buttonRow.add(deleteButton);
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
    // Helper methods: labels, read-only fields, styled buttons
    // ---------------------------------------------------------

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        return label;
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField(20);
        field.setFont(FONT_FIELD);
        field.setEditable(false);
        field.setBackground(COLOR_READONLY_BG);
        return field;
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
    // Business logic: search + delete
    // ---------------------------------------------------------

    /**
     * Handles the Search button click:
     * 1. Reads and validates the Supplier ID.
     * 2. Fetches the supplier via SupplierDAO.getSupplierById().
     * 3. Populates the read-only fields on success, so the user can
     *    confirm they're about to delete the right record.
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
                clearResultFields();
                loadedSupplierId = null;
                return;
            }

            supplierNameValue.setText(supplier.getSupplierName());
            companyNameValue.setText(supplier.getCompanyName());
            phoneValue.setText(supplier.getPhone());
            emailValue.setText(supplier.getEmail());
            addressValue.setText(supplier.getAddress());
            gstNumberValue.setText(supplier.getGstNumber());

            // Remember which supplier is loaded, so Delete always acts
            // on the record actually shown on screen.
            loadedSupplierId = supplier.getSupplierId();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while searching: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the Delete button click:
     * 1. Ensures a supplier has actually been searched and loaded.
     * 2. Asks for confirmation before deleting.
     * 3. Deletes the record via SupplierDAO.deleteSupplier().
     * 4. Shows a success or error message and clears the form on success.
     */
    private void handleDelete() {
        if (loadedSupplierId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please search for a supplier first (enter Supplier ID and click Search).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete supplier \"" + supplierNameValue.getText() + "\" (ID: "
                        + loadedSupplierId + ")? This action cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean isDeleted = supplierDAO.deleteSupplier(loadedSupplierId);

            if (isDeleted) {
                JOptionPane.showMessageDialog(this,
                        "Supplier deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearAllFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Delete failed. The supplier may have already been removed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while deleting: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all read-only result fields (used when a search finds
     * no matching supplier).
     */
    private void clearResultFields() {
        supplierNameValue.setText("");
        companyNameValue.setText("");
        phoneValue.setText("");
        emailValue.setText("");
        addressValue.setText("");
        gstNumberValue.setText("");
    }

    /**
     * Clears the Supplier ID field, all result fields, and resets the
     * loaded supplier tracker (used after a successful delete).
     */
    private void clearAllFields() {
        supplierIdField.setText("");
        clearResultFields();
        loadedSupplierId = null;
    }
}