package com.grocery.gui;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

/**
 * UpdateProductFrame
 * --------------------
 * Full-screen screen for updating an existing product, opened from
 * MainDashboard.
 *
 * Flow:
 *   1. User enters Product ID and clicks Search — fields populate from DB.
 *   2. User edits any field(s) and clicks Update Product to save changes.
 *
 * Uses ProductDAO / ProductDAOImpl for all database operations.
 */
public class UpdateProductFrame extends JFrame {

    // Theme palette
    private static final Color PRIMARY_GREEN = new Color(46, 125, 50);   // #2E7D32
    private static final Color LIGHT_BG = new Color(232, 245, 233);      // #E8F5E9
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color CARD_BORDER = new Color(210, 210, 210);
    private static final Color BACK_GRAY = new Color(117, 117, 117);

    // DAO instance used for search and update operations
    private final ProductDAO dao = new ProductDAOImpl();

    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField supplierField;
    private JTextField expiryDateField;

    private JButton searchBtn;
    private JButton updateBtn;
    private JButton backBtn;

    public UpdateProductFrame() {
        initFrame();
        initHeader();
        initFormPanel();
        setVisible(true);
    }

    /**
     * Configures core JFrame properties. Window opens maximized (full screen)
     * but remains resizable.
     */
    private void initFrame() {
        setTitle("Grocery Management System - Update Product");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // only closes this window
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open full screen
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);
    }

    /**
     * Top header bar showing the screen title.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("✏️  UPDATE PRODUCT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center area: a centered white "card" panel containing the search
     * row (Product ID + Search button), the editable product fields,
     * and the Update/Back buttons.
     */
    private void initFormPanel() {
        // Outer wrapper centers the card both horizontally and vertically
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(LIGHT_BG);

        // The white card itself
        JPanel card = new JPanel();
        card.setBackground(CARD_WHITE);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(540, 620));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Card heading
        JLabel cardHeading = new JLabel("Search &amp; Update Product");
        cardHeading.setText("Search & Update Product");
        cardHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardHeading.setForeground(PRIMARY_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 20, 5);
        card.add(cardHeading, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Product ID label + field
        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        card.add(productIdLabel, gbc);

        productIdField = new JTextField();
        productIdField.setFont(fieldFont);
        productIdField.setPreferredSize(new Dimension(220, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        card.add(productIdField, gbc);

        // Search button
        searchBtn = createStyledButton("🔍  Search", PRIMARY_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        card.add(searchBtn, gbc);
        gbc.insets = new Insets(10, 5, 10, 5);

        searchBtn.addActionListener(e -> handleSearch());

        // Separator line before editable fields
        JSeparator separator = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        card.add(separator, gbc);
        gbc.gridwidth = 1;

        // ---- Editable product fields ----
        productNameField = addEditableRow(card, gbc, 4, "Product Name:", labelFont, fieldFont);
        categoryField = addEditableRow(card, gbc, 5, "Category:", labelFont, fieldFont);
        priceField = addEditableRow(card, gbc, 6, "Price (₹):", labelFont, fieldFont);
        quantityField = addEditableRow(card, gbc, 7, "Quantity:", labelFont, fieldFont);
        supplierField = addEditableRow(card, gbc, 8, "Supplier:", labelFont, fieldFont);
        expiryDateField = addEditableRow(card, gbc, 9, "Expiry Date (yyyy-MM-dd):", labelFont, fieldFont);

        // Button row: Update Product + Back, side by side
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(CARD_WHITE);

        updateBtn = createStyledButton("💾  Update Product", PRIMARY_GREEN);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        updateBtn.addActionListener(e -> handleUpdate());
        backBtn.addActionListener(e -> {
            new ProductManagementFrame();
            dispose();
        });
        buttonRow.add(updateBtn);
        buttonRow.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        card.add(buttonRow, gbc);

        outerWrapper.add(card); // GridBagLayout with no constraints = centers the component

        add(outerWrapper, BorderLayout.CENTER);
    }

    /**
     * Handles the Search button click:
     * 1. Reads and validates the Product ID.
     * 2. Fetches the product via ProductDAO.getProductById().
     * 3. Populates the editable fields on success, or shows an error
     *    message if not found or if input is invalid.
     */
    private void handleSearch() {
        String idText = productIdField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Product ID.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Product ID must be a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Product product = dao.getProductById(productId);

            if (product == null) {
                JOptionPane.showMessageDialog(this, "Product not found!");
                clearEditableFields();
                return;
            }

            // Populate editable fields with the fetched product's data
            productNameField.setText(product.getProductName());
            categoryField.setText(product.getCategory());
            priceField.setText(product.getPrice() != null ? product.getPrice().toString() : "");
            quantityField.setText(String.valueOf(product.getQuantity()));
            supplierField.setText(product.getSupplier());
            expiryDateField.setText(product.getExpiryDate() != null ? product.getExpiryDate().toString() : "");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while searching: " + ex.getMessage(),
                    "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the Update Product button click:
     * 1. Validates the Product ID and all editable fields.
     * 2. Converts field values into proper types.
     * 3. Builds a Product object and updates it via ProductDAO.updateProduct().
     * 4. Shows success/failure message.
     */
    private void handleUpdate() {
        String idText = productIdField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please search for a product first (enter Product ID and click Search).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Product ID must be a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productName = productNameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String supplier = supplierField.getText().trim();
        String expiryDateText = expiryDateField.getText().trim();

        if (productName.isEmpty() || category.isEmpty() || priceText.isEmpty()
                || quantityText.isEmpty() || supplier.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        // Build the Product object with the updated values
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSupplier(supplier);
        product.setExpiryDate(expiryDate);

        try {
        	System.out.println("Product ID = " + product.getProductId());
        	System.out.println(product);
            boolean isUpdated = dao.updateProduct(product);

            if (isUpdated) {
                JOptionPane.showMessageDialog(this,
                        "Product Updated Successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Update Failed!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while updating: " + ex.getMessage(),
                    "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all editable fields (used when a search finds no matching product).
     */
    private void clearEditableFields() {
        productNameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        supplierField.setText("");
        expiryDateField.setText("");
    }

    /**
     * Helper that adds one label + editable text field row to the card
     * at the given grid row, and returns the created field.
     */
    private JTextField addEditableRow(JPanel panel, GridBagConstraints gbc, int row,
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
     * Helper to create a consistently styled, large button with a custom
     * background color.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }
  
}