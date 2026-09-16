package com.grocery.gui;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;
import com.grocery.model.Supplier;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * ProductManagementFrame
 * ------------------------
 * Enterprise-style dashboard for the Product module, opened from
 * SupplierDashboard's Products menu.
 *
 * This frame does NOT contain any CRUD logic itself — it is a
 * navigation and statistics hub only. Its buttons open the existing,
 * already-working frames:
 * - AddProductFrame
 * - ViewProductsFrame
 * - SearchProductFrame
 * - UpdateProductFrame
 * - DeleteProductFrame
 *
 * Layout:
 * - NORTH:  Header (title + subtitle)
 * - WEST:   Sidebar with quick-navigation action buttons
 * - CENTER: Dashboard cards showing quick statistics (Total Products,
 *           Active Products, Out of Stock, Categories)
 */
public class ProductManagementFrame extends JFrame {

    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color DELETE_RED = new Color(198, 40, 40);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color SIDEBAR_BG = new Color(21, 71, 24);
    private static final Color CARD_BG = Color.WHITE;
    private static final Font SEGOE_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font SEGOE_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SEGOE_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font SEGOE_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private Supplier loggedInSupplier;
    private boolean isSupplier = false;
    // DAO instance used only for read-only quick statistics on this dashboard
    private final ProductDAO productDAO = new ProductDAOImpl();

    private JButton addProductBtn;
    private JButton viewProductsBtn;
    private JButton searchProductBtn;
    private JButton updateProductBtn;
    private JButton deleteProductBtn;
    private JButton backBtn;

    private JLabel totalProductsValue;
    private JLabel activeProductsValue;
    private JLabel outOfStockValue;
    private JLabel categoriesValue;

    public ProductManagementFrame() {
        initFrame();
        initHeader();
        initSidebar();
        initDashboardCards();
        loadStatistics();

        setVisible(true);
    }
    public ProductManagementFrame(Supplier supplier) {

        System.out.println("SUPPLIER CONSTRUCTOR CALLED");

        this.loggedInSupplier = supplier;
        this.isSupplier = true;

        initFrame();
        initHeader();
        initSidebar();
        initDashboardCards();
        loadStatistics();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {
        setTitle("Grocery ERP - Product Management");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GREEN);
    }

    /**
     * Top header showing the screen title and subtitle.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel titleLabel = new JLabel("📦  Product Management");
        titleLabel.setFont(SEGOE_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Manage all supplier products efficiently");
        subtitleLabel.setFont(SEGOE_SUBTITLE);
        subtitleLabel.setForeground(new Color(220, 237, 220));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Left sidebar containing navigation buttons that open the existing
     * Product CRUD frames. Each button opens its target frame and closes
     * this dashboard, matching the Back-button pattern used elsewhere
     * in the app.
     */
    private void initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        addProductBtn = createSidebarButton("➕  Add Product");
        viewProductsBtn = createSidebarButton("📋  View Products");
        searchProductBtn = createSidebarButton("🔍  Search Product");
        updateProductBtn = createSidebarButton("✏️  Update Product");
        deleteProductBtn = createSidebarButton("🗑️  Delete Product");
        backBtn = createSidebarButton("⬅  Back to Dashboard");

        addProductBtn.addActionListener(e -> {

            new AddProductFrame();

            dispose();

        });
        viewProductsBtn.addActionListener(e -> {
            new ViewProductsFrame();
            dispose();
        });
        searchProductBtn.addActionListener(e -> {
            new SearchProductFrame();
            dispose();
        });
        updateProductBtn.addActionListener(e -> {
            new UpdateProductFrame();
            dispose();
        });
        deleteProductBtn.addActionListener(e -> {
            new DeleteProductFrame();
            dispose();
        });
        backBtn.addActionListener(e -> {

            dispose();

            if(isSupplier){

                new SupplierDashboard(loggedInSupplier);

            }
            else{

                new MainDashboard();

            }

        });
       
        sidebar.add(addProductBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(viewProductsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(searchProductBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(updateProductBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        sidebar.add(deleteProductBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(backBtn);

        add(sidebar, BorderLayout.WEST);
    }

    /**
     * Center area with quick-statistics dashboard cards: Total Products,
     * Active Products, Out of Stock, and Total Categories.
     */
    private void initDashboardCards() {
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(LIGHT_GREEN);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel totalCard = createStatCard("Total Products", BUTTON_GREEN);
        totalProductsValue = (JLabel) totalCard.getClientProperty("valueLabel");

        JPanel activeCard = createStatCard("Active Products", new Color(46, 125, 50));
        activeProductsValue = (JLabel) activeCard.getClientProperty("valueLabel");

        JPanel outOfStockCard = createStatCard("Out of Stock", DELETE_RED);
        outOfStockValue = (JLabel) outOfStockCard.getClientProperty("valueLabel");

        JPanel categoriesCard = createStatCard("Total Categories", RESET_GRAY);
        categoriesValue = (JLabel) categoriesCard.getClientProperty("valueLabel");

        cardsPanel.add(totalCard);
        cardsPanel.add(activeCard);
        cardsPanel.add(outOfStockCard);
        cardsPanel.add(categoriesCard);

        add(cardsPanel, BorderLayout.CENTER);
    }

    /**
     * Helper to build a single dashboard statistic card: a white,
     * bordered panel with a large value label and a caption underneath.
     * The value JLabel is stashed as a client property so the caller
     * can retrieve and update it later via loadStatistics().
     */
    private JPanel createStatCard(String label, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel accentBar = new JLabel(" ");
        accentBar.setOpaque(true);
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(40, 4));
        accentBar.setMaximumSize(new Dimension(40, 4));
        accentBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel("0");
        valueLabel.setFont(SEGOE_CARD_VALUE);
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel captionLabel = new JLabel(label);
        captionLabel.setFont(SEGOE_CARD_LABEL);
        captionLabel.setForeground(new Color(90, 90, 90));
        captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(accentBar);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(captionLabel);

        card.putClientProperty("valueLabel", valueLabel);
        return card;
    }

    /**
     * Loads quick statistics from ProductDAO and populates the
     * dashboard cards. Read-only — does not modify any product data.
     */
    private void loadStatistics() {
        try {
            int total = productDAO.getTotalProducts();
            totalProductsValue.setText(String.valueOf(total));

            List<Product> activeProducts = productDAO.getProductsByStatus("ACTIVE");
            activeProductsValue.setText(String.valueOf(activeProducts.size()));

            List<Product> outOfStockProducts = productDAO.getProductsByStatus("OUT_OF_STOCK");
            outOfStockValue.setText(String.valueOf(outOfStockProducts.size()));

            // Temporary until getAllCategories() is added
            categoriesValue.setText("0");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load product statistics: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper to create a consistently styled sidebar navigation button.
     */
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(SIDEBAR_BG);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(190, 42));
        button.setPreferredSize(new Dimension(190, 42));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
}