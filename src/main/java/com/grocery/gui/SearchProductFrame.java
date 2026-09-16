package com.grocery.gui;
import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
/**
 * SearchProductFrame
 * --------------------
 * Full-screen screen for searching a single product by Product ID,
 * opened from MainDashboard.
 *
 * This step builds ONLY the UI shell — Search shows a placeholder message
 * and result fields stay empty/read-only. ProductDAO.getProductById()
 * will be connected in a future step to populate the result fields.
 */
public class SearchProductFrame extends JFrame {

    // Same theme palette as AddProductFrame
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color CARD_BORDER = new Color(210, 210, 210);
    private static final Color READONLY_BG = new Color(245, 245, 245);

    private JTextField productIdField;
    private JButton searchBtn;
    private JButton backBtn;

    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField supplierField;
    private JTextField expiryDateField;

    public SearchProductFrame() {
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
        setTitle("Grocery Management System - Search Product");
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

        JLabel titleLabel = new JLabel("🔍  Search Product");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center area: a centered white "card" panel containing the search
     * row (Product ID + Search button), the read-only result fields,
     * and the Back button — all matching AddProductFrame's card style.
     */
    private void initFormPanel() {
        // Outer wrapper centers the card both horizontally and vertically
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(LIGHT_GREEN);

        // The white card itself
        JPanel card = new JPanel();
        card.setBackground(CARD_WHITE);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(520, 560));
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
        JLabel cardHeading = new JLabel("Search Product by ID");
        cardHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardHeading.setForeground(DARK_GREEN);
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

        // Search button (spans both columns, right below Product ID row)
        searchBtn = createStyledButton("🔍  Search", BUTTON_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        card.add(searchBtn, gbc);
        gbc.insets = new Insets(10, 5, 10, 5);
        searchBtn.addActionListener(e -> {

            try {

                int id = Integer.parseInt(productIdField.getText());

                ProductDAO dao = new ProductDAOImpl();

                Product product = dao.getProductById(id);

                if (product != null) {

                    productNameField.setText(product.getProductName());
                    categoryField.setText(product.getCategory());
                    priceField.setText(product.getPrice().toString());
                    quantityField.setText(String.valueOf(product.getQuantity()));
                    supplierField.setText(product.getSupplier());
                    expiryDateField.setText(product.getExpiryDate().toString());

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Product not found!");

                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this,
                        "Enter a valid Product ID.");

            }
            catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database Error!");
            }

        });

        // Separator line before result fields
        JSeparator separator = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        card.add(separator, gbc);
        gbc.gridwidth = 1;

        // ---- Read-only result fields ----

        productNameField = addReadOnlyRow(card, gbc, 4, "Product Name:", labelFont, fieldFont);
        categoryField = addReadOnlyRow(card, gbc, 5, "Category:", labelFont, fieldFont);
        priceField = addReadOnlyRow(card, gbc, 6, "Price (₹):", labelFont, fieldFont);
        quantityField = addReadOnlyRow(card, gbc, 7, "Quantity:", labelFont, fieldFont);
        supplierField = addReadOnlyRow(card, gbc, 8, "Supplier:", labelFont, fieldFont);
        expiryDateField = addReadOnlyRow(card, gbc, 9, "Expiry Date:", labelFont, fieldFont);

        // Back button
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        card.add(backBtn, gbc);

        backBtn.addActionListener(e -> {
        	new ProductManagementFrame();
            dispose();
        });

        outerWrapper.add(card); // GridBagLayout with no constraints = centers the component

        add(outerWrapper, BorderLayout.CENTER);
    }

    /**
     * Helper that adds one label + read-only text field row to the card
     * at the given grid row, and returns the created field.
     */
    private JTextField addReadOnlyRow(JPanel panel, GridBagConstraints gbc, int row,
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
        field.setEditable(false);
        field.setBackground(READONLY_BG);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(field, gbc);

        return field;
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
        button.setPreferredSize(new Dimension(160, 38));
        return button;
    }
}