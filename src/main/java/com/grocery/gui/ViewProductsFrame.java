package com.grocery.gui;
import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * ViewProductsFrame
 * ------------------
 * Full-screen screen for viewing all products in a tabular format,
 * opened from MainDashboard.
 *
 * This step builds ONLY the UI shell — the JTable is empty and Refresh
 * shows a placeholder message. ProductDAO.getAllProducts() will be
 * connected in a future step to populate the table with live data.
 */
public class ViewProductsFrame extends JFrame {

    // Same theme palette as AddProductFrame
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color CARD_BORDER = new Color(210, 210, 210);
    private static final Color TABLE_HEADER_GREEN = new Color(46, 125, 50);

    private JTable productTable;
    private DefaultTableModel tableModel;

    private JButton refreshBtn;
    private JButton backBtn;

    public ViewProductsFrame() {
        initFrame();
        initHeader();
        initTablePanel();
        initButtonPanel();
        loadProductData();
        
        setVisible(true);
    }

    /**
     * Configures core JFrame properties. Window opens maximized (full screen)
     * but remains resizable.
     */
    private void initFrame() {
        setTitle("Grocery Management System - View Products");
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

        JLabel titleLabel = new JLabel("📦  View Products");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center area: a centered white "card" panel containing the heading
     * and the product table (inside a JScrollPane). Unlike smaller forms,
     * this card stretches to fill most of the screen (with generous
     * margins) since a table needs room to breathe — but it still never
     * touches the window edges, keeping the same card-based look as
     * AddProductFrame.
     */
    private void initTablePanel() {
        // Outer wrapper adds a consistent margin around the card so it
        // never touches the window edges, even in full screen.
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(LIGHT_GREEN);
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(30, 60, 20, 60));

        // The white card itself
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        // Card heading
        JLabel cardHeading = new JLabel("All Products");
        cardHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardHeading.setForeground(DARK_GREEN);
        cardHeading.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        card.add(cardHeading, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {
                "Product ID", "Product Name", "Category", "Price",
                "Quantity", "Supplier", "Expiry Date"
        };

        // Empty table model — no rows added yet. Non-editable so users
        // can't edit cells directly in the table.
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        productTable.setRowHeight(28);
        productTable.setSelectionBackground(new Color(200, 230, 201));
        productTable.setSelectionForeground(Color.BLACK);
        productTable.setGridColor(new Color(224, 224, 224));
        productTable.setShowGrid(true);

        // Style the table header to match the green theme
        productTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(TABLE_HEADER_GREEN);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 34));

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER));

        card.add(scrollPane, BorderLayout.CENTER);

        outerWrapper.add(card, BorderLayout.CENTER);
        add(outerWrapper, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Refresh and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        refreshBtn = createStyledButton("🔄  Refresh", BUTTON_GREEN);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        refreshBtn.addActionListener(e -> {
            loadProductData();
        });

        backBtn.addActionListener(e -> {
        	new ProductManagementFrame();
            dispose();
        });

        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
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
        button.setPreferredSize(new Dimension(160, 40));
        return button;}
      

    private void loadProductData() {

        tableModel.setRowCount(0);

        try {

            ProductDAO productDAO = new ProductDAOImpl();

            List<Product> productList = productDAO.getAllProducts();

            if(productList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Products Found");
                return;
            }

            for(Product product : productList) {

                tableModel.addRow(new Object[] {
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getSupplier(),
                    product.getExpiryDate()
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    }
