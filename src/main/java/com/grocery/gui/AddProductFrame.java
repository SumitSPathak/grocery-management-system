package com.grocery.gui;
import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.*;
import java.awt.*;

/**
 * AddProductFrame
 * ----------------
 * Full-screen screen for adding a new product, opened from MainDashboard.
 *
 * The form is placed inside a centered white "card" panel rather than
 * stretching across the whole window, for a modern, professional look.
 *
 * This step builds ONLY the UI shell — no database operations are wired
 * yet. Save button currently validates input and shows a placeholder
 * confirmation; ProductDAO.addProduct() will be connected in a future step.
 */
public class AddProductFrame extends JFrame {

    // Same theme palette as the Admin module
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color CARD_BORDER = new Color(210, 210, 210);

    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField supplierField;
    private JTextField expiryDateField; // format: yyyy-MM-dd

    private JButton saveBtn;
    private JButton resetBtn;
    private JButton backBtn;

    public AddProductFrame() {
        initFrame();
        initHeader();
        initFormPanel();
        initButtonPanel();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties. Window opens maximized (full screen)
     * but remains resizable, so the user can still restore/resize it manually.
     */
    private void initFrame() {
        setTitle("Grocery Management System - Add Product");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // only closes this window
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open full screen
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GREEN);
    }

    /**
     * Top header bar showing the screen title.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("➕  Add Product");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center area: uses GridBagLayout with no constraints filling, so the
     * white "card" panel stays a fixed comfortable size and remains
     * centered on screen regardless of window size (including full screen).
     */
    private void initFormPanel() {
        // Outer wrapper centers the card both horizontally and vertically
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(LIGHT_GREEN);

        // The white card itself
        JPanel card = new JPanel();
        card.setBackground(CARD_WHITE);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(500, 480));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Card heading inside the white panel
        JLabel cardHeading = new JLabel("Enter Product Details");
        cardHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardHeading.setForeground(DARK_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 20, 5);
        card.add(cardHeading, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Product Name
        productNameField = addFormRow(card, gbc, 1, "Product Name:", labelFont, fieldFont);

        // Category
        categoryField = addFormRow(card, gbc, 2, "Category:", labelFont, fieldFont);

        // Price
        priceField = addFormRow(card, gbc, 3, "Price (₹):", labelFont, fieldFont);

        // Quantity
        quantityField = addFormRow(card, gbc, 4, "Quantity:", labelFont, fieldFont);

        // Supplier
        supplierField = addFormRow(card, gbc, 5, "Supplier:", labelFont, fieldFont);

        // Expiry Date
        expiryDateField = addFormRow(card, gbc, 6, "Expiry Date (yyyy-MM-dd):", labelFont, fieldFont);

        outerWrapper.add(card); // GridBagLayout with no constraints = centers the component

        add(outerWrapper, BorderLayout.CENTER);
    }

    /**
     * Helper that adds one label + text field row to the given panel at
     * the given grid row, and returns the created field so the caller
     * can store it in a class field. Avoids repeating the same
     * GridBagConstraints code six times.
     */
    private JTextField addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                                   String labelText, Font labelFont, Font fieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.4;
        panel.add(label, gbc);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(field, gbc);

        return field;
    }

    /**
     * Bottom panel with Save, Reset, and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        saveBtn = createStyledButton("💾  Save", BUTTON_GREEN);
        resetBtn = createStyledButton("Reset", RESET_GRAY);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        saveBtn.addActionListener(e -> handleSave());
        resetBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> {
            new ProductManagementFrame();
            dispose();
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the Save button click. Validates input fields only.
     * Database insertion (ProductDAO.addProduct()) will be wired in a
     * future step.
     */
    /**
     * Handles the Save button click:
     * 1. Validates all required fields are filled.
     * 2. Converts field values into proper types (BigDecimal, int, sql.Date).
     * 3. Builds a Product object and inserts it via ProductDAO.
     * 4. Shows success/error message and clears the form on success.
     */
    private void handleSave() {
        String productName = productNameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String supplier = supplierField.getText().trim();
        String expiryDateText = expiryDateField.getText().trim();

        // ---- Basic field validation ----
        if (productName.isEmpty() || category.isEmpty() || priceText.isEmpty()
                || quantityText.isEmpty() || supplier.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- Type conversion with error handling ----
        BigDecimal price;
        int quantity;
        Date expiryDate = null;

        try {
            price = new BigDecimal(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid price (e.g. 49.99).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid whole number for quantity.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Expiry date is optional, but if provided it must be in yyyy-MM-dd format
        if (!expiryDateText.isEmpty()) {
            try {
                expiryDate = Date.valueOf(expiryDateText);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter expiry date in yyyy-MM-dd format.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // ---- Build Product object and insert into database ----
        Product product = new Product();

        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSupplier(supplier);
        product.setExpiryDate(expiryDate);

        ProductDAO dao = new ProductDAOImpl();
        boolean isSaved = false;

        try {
            isSaved = dao.addProduct(product);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isSaved) {
            JOptionPane.showMessageDialog(this,
                    "Product Added Successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            productNameField.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add product. Please try again.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all form fields.
     */
    private void resetForm() {
        productNameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        supplierField.setText("");
        expiryDateField.setText("");
        productNameField.requestFocus();
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
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }
}