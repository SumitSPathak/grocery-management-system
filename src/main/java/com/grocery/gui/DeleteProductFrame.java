package com.grocery.gui;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

import javax.swing.*;
import java.awt.*;

/**
 * DeleteProductFrame
 * -------------------
 * Screen for searching a product by ID and permanently deleting it,
 * opened from MainDashboard.
 *
 * Flow:
 * 1. User enters a Product ID and clicks Search.
 * 2. Product details are fetched via ProductDAO.getProductById() and
 *    displayed in read-only fields.
 * 3. User clicks Delete Product, confirms via a dialog, and the record
 *    is removed via ProductDAO.deleteProduct().
 *
 * This class belongs to the GUI layer only — all persistence is handled
 * by the existing ProductDAO / ProductDAOImpl.
 */
public class DeleteProductFrame extends JFrame {

    // Same theme palette as SearchProductFrame / UpdateProductFrame
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color DELETE_RED = new Color(198, 40, 40);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color CARD_BG = Color.WHITE;

    // DAO instance used to fetch and delete product records
    private final ProductDAO productDAO = new ProductDAOImpl();

    // Search section
    private JTextField productIdField;
    private JButton searchBtn;

    // Read-only result fields
    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField supplierField;
    private JTextField expiryDateField;

    // Bottom buttons
    private JButton deleteBtn;
    private JButton backBtn;

    // Holds the currently loaded product's ID for the Delete action
    private Integer currentProductId;

    public DeleteProductFrame() {
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
    	    setTitle("Grocery Management System - Delete Product");

    	    // Full Screen
    	    setExtendedState(JFrame.MAXIMIZED_BOTH);

    	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    	    setLayout(new BorderLayout());

    	    getContentPane().setBackground(LIGHT_GREEN);

    	    setLocationRelativeTo(null);

    	    setResizable(true);
    	}
    /**
     * Top header showing the screen title.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("🗑️  Delete Product");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }/**
     * Builds the search row (Product ID field + Search button) and the
     * white card containing the read-only product detail fields.
     */
    private void initFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(LIGHT_GREEN);
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // ---- Search row: Product ID label + field + Search button ----
        JPanel searchRow = new JPanel(new GridBagLayout());
        searchRow.setBackground(LIGHT_GREEN);
        searchRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        searchRow.add(productIdLabel, gbc);

        productIdField = new JTextField();
        productIdField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchRow.add(productIdField, gbc);

        searchBtn = createStyledButton("🔍  Search", BUTTON_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 5, 5, 5);
        searchRow.add(searchBtn, gbc);

        formPanel.add(searchRow, BorderLayout.NORTH);

        // ---- White card with read-only product detail fields ----
        JPanel resultCard = new JPanel();
        resultCard.setBackground(CARD_BG);
        resultCard.setLayout(new GridBagLayout());
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(10, 10, 10, 10);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 14);

        int row = 0;
        productNameField = addReadOnlyRow(resultCard, "Product Name:", fgbc, row++, labelFont, valueFont);
        categoryField = addReadOnlyRow(resultCard, "Category:", fgbc, row++, labelFont, valueFont);
        priceField = addReadOnlyRow(resultCard, "Price:", fgbc, row++, labelFont, valueFont);
        quantityField = addReadOnlyRow(resultCard, "Quantity:", fgbc, row++, labelFont, valueFont);
        supplierField = addReadOnlyRow(resultCard, "Supplier:", fgbc, row++, labelFont, valueFont);
        expiryDateField = addReadOnlyRow(resultCard, "Expiry Date:", fgbc, row++, labelFont, valueFont);

        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(LIGHT_GREEN);
        cardWrapper.add(resultCard, BorderLayout.CENTER);

        formPanel.add(cardWrapper, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> handleSearch());

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Delete Product and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        deleteBtn = createStyledButton("🗑️  Delete Product", DELETE_RED);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        deleteBtn.addActionListener(e -> handleDelete());
        backBtn.addActionListener(e -> {
            new ProductManagementFrame();
            dispose();
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }/**
     * Handles the Search button click:
     * 1. Validates that a Product ID was entered and is numeric.
     * 2. Looks up the product via ProductDAO.getProductById().
     * 3. Populates the read-only fields on success, or shows
     *    "Product Not Found." and clears fields on failure.
     * 4. Any SQLException/runtime failure from the DAO layer is
     *    surfaced to the user via a dialog instead of crashing the UI.
     */
    private void handleSearch() {
        String idText = productIdField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Product ID.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Product ID. Please enter a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Product product = productDAO.getProductById(productId);

            if (product != null) {
                currentProductId = productId;
                productNameField.setText(product.getProductName());
                categoryField.setText(product.getCategory());
                priceField.setText(String.valueOf(product.getPrice()));
                quantityField.setText(String.valueOf(product.getQuantity()));
                supplierField.setText(product.getSupplier());
                expiryDateField.setText(String.valueOf(product.getExpiryDate()));
            } else {
                currentProductId = null;
                JOptionPane.showMessageDialog(this,
                        "Product Not Found.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                clearFields();
            }
        } catch (Exception ex) {
            currentProductId = null;
            JOptionPane.showMessageDialog(this,
                    "Database error while searching: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            clearFields();
        }
    }

    /**
     * Handles the Delete Product button click:
     * 1. Validates that a product has already been loaded via Search.
     * 2. Asks the user to confirm the destructive action.
     * 3. Calls ProductDAO.deleteProduct(productId).
     * 4. Shows a success or failure message based on the result, and
     *    clears all fields after a successful delete.
     */
    private void handleDelete() {
        if (currentProductId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please search for a product before deleting.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = productDAO.deleteProduct(currentProductId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Product Deleted Successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Delete Failed!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while deleting: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }/**
     * Helper to add a labeled, read-only row to the result card using
     * GridBagLayout. Returns the created JTextField so the caller can
     * populate it later.
     */
    private JTextField addReadOnlyRow(JPanel panel, String labelText, GridBagConstraints gbc,
                                       int row, Font labelFont, Font valueFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.4;
        panel.add(label, gbc);

        JTextField valueField = new JTextField();
        valueField.setFont(valueFont);
        valueField.setEditable(false);
        valueField.setFocusable(false);
        valueField.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(valueField, gbc);

        return valueField;
    }

    /**
     * Clears the Product ID field, all read-only result fields, and
     * resets the currently loaded product reference.
     */
    private void clearFields() {
        productIdField.setText("");
        productNameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        supplierField.setText("");
        expiryDateField.setText("");
        currentProductId = null;
        productIdField.requestFocus();
    }

    /**
     * Helper to create a consistently styled button with a custom
     * background color, matching the theme used across the app.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 38));
        return button;
    }

    /**
     * Application entry point for standalone testing of this screen.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeleteProductFrame::new);
    }
}