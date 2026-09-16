package com.grocery.gui;
import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import javax.swing.*;
import java.awt.*;

/**
 * MainDashboard
 * -------------
 * The main landing window (JFrame) of the Grocery Management System desktop app.
 *
 * This class belongs to the GUI layer only. It has no database or business
 * logic — its sole responsibility is to display the dashboard and open
 * other Swing windows when the corresponding button is clicked.
 *
 * Phase 5 additions: welcome/status section and Logout button.
 * All existing product button functionality is unchanged.
 */
public class MainDashboard extends JFrame {

    // Theme colors defined as constants so every screen reuses the exact
    // same palette for visual consistency.
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color LOGOUT_RED = new Color(198, 40, 40);
    private static final Color STATUS_GREEN = new Color(46, 125, 50);

    // Buttons are declared as fields so ActionListeners can be attached
    // without redesigning the layout.
    private JButton addProductBtn;
    private JButton viewProductsBtn;
    private JButton searchProductBtn;
    private JButton updateProductBtn;
    private JButton deleteProductBtn;
    private JButton adminManagementBtn;
    private JButton supplierManagementBtn;
    private JButton employeeManagementBtn;
    private JButton purchaseManagementBtn;
    private JButton salesBtn;
    private JButton reportsBtn;
    private JButton settingsBtn;
    private JButton exitBtn;
    private JButton logoutBtn;
    private JLabel totalProductsLabel;
    private JLabel totalCategoriesLabel;
    private JLabel lowStockLabel;
    private JLabel outOfStockLabel;
    private JLabel expiredProductsLabel;
    private JPanel centerPanel;

    private ProductDAO productDAO = new ProductDAOImpl();
    public MainDashboard() {
    	initFrame();
    	initHeader();
    	initStatisticsPanel();
    	loadDashboardData();
    	initButtonPanel();
    	initFooter();

    	setVisible(true);
    }

    /**
     * Configures core JFrame properties: title, size, close behavior,
     * and centers the window on screen.
     */
    private void initFrame() {

        setTitle("Grocery Management System - Dashboard");

        // Full Screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Allow Resize
        setResizable(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(LIGHT_GREEN);
    }

    /**
     * Builds the top section: title bar (with Logout button) plus a
     * welcome/status panel below it.
     */
    private void initHeader() {
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BorderLayout());
        topWrapper.setBackground(DARK_GREEN);

        // ---- Title bar row: title on left, Logout button on right ----
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(DARK_GREEN);
        titleBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 15));

        JLabel titleLabel = new JLabel("🛒 Grocery Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel, BorderLayout.WEST);

        logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setBackground(LOGOUT_RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        titleBar.add(logoutBtn, BorderLayout.EAST);

        topWrapper.add(titleBar, BorderLayout.NORTH);

        // ---- Welcome / status section ----
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(DARK_GREEN);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Welcome, Sumit Pathak");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("Role: ADMIN");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        roleLabel.setForeground(new Color(220, 237, 220));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("● Status: Online");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setForeground(new Color(129, 199, 132)); // light green "online" dot
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        welcomePanel.add(roleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        welcomePanel.add(statusLabel);

        topWrapper.add(welcomePanel, BorderLayout.CENTER);

        add(topWrapper, BorderLayout.NORTH);
    }

    /**
     * Builds the center panel containing the main navigation buttons,
     * arranged in a clean grid. Unchanged from previous version.
     */
  
    	private void initStatisticsPanel() {

    	    centerPanel = new JPanel();
    	    centerPanel.setLayout(new BorderLayout());
    	    centerPanel.setBackground(LIGHT_GREEN);

    	    JPanel statsPanel = new JPanel(new GridLayout(1, 5, 15, 15));
    	    statsPanel.setBackground(LIGHT_GREEN);
    	    statsPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    	    totalProductsLabel = createStatCard("Total Products", "0");
    	    totalCategoriesLabel = createStatCard("Categories", "0");
    	    lowStockLabel = createStatCard("Low Stock", "0");
    	    outOfStockLabel = createStatCard("Out of Stock", "0");
    	    expiredProductsLabel = createStatCard("Expired", "0");

    	    statsPanel.add(totalProductsLabel);
    	    statsPanel.add(totalCategoriesLabel);
    	    statsPanel.add(lowStockLabel);
    	    statsPanel.add(outOfStockLabel);
    	    statsPanel.add(expiredProductsLabel);

    	    centerPanel.add(statsPanel, BorderLayout.NORTH);

    	    add(centerPanel, BorderLayout.CENTER);
    	}
    
   
	private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new GridLayout(13, 1, 10, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        addProductBtn = createStyledButton("➕  Add Product");
        viewProductsBtn = createStyledButton("📦  View Products");
        searchProductBtn = createStyledButton("🔍  Search Product");
        updateProductBtn = createStyledButton("✏️  Update Product");
        deleteProductBtn = createStyledButton("🗑️  Delete Product");
        adminManagementBtn = createStyledButton("👑 Admin Management");
        supplierManagementBtn = createStyledButton("🚚 Supplier Management");
        employeeManagementBtn = createStyledButton("👨 Employee Management");
        purchaseManagementBtn = createStyledButton("🛒 Purchase Management");
        salesBtn = createStyledButton("💰 Sales");
        reportsBtn = createStyledButton("📈 Reports");
        settingsBtn = createStyledButton("⚙️ Settings");
        // 👇 YEH YAHAN ADD KARNA HAI
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
        
        adminManagementBtn.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });
        
        supplierManagementBtn.addActionListener(e -> {
            new SupplierManagementFrame();
            dispose();
        });
        employeeManagementBtn.addActionListener(e ->
        JOptionPane.showMessageDialog(this, "Employee Module Coming Soon"));

    purchaseManagementBtn.addActionListener(e ->
        JOptionPane.showMessageDialog(this, "Purchase Module Coming Soon"));

    salesBtn.addActionListener(e ->
        JOptionPane.showMessageDialog(this, "Sales Module Coming Soon"));

    reportsBtn.addActionListener(e ->
        JOptionPane.showMessageDialog(this, "Reports Module Coming Soon"));

    settingsBtn.addActionListener(e ->
        JOptionPane.showMessageDialog(this, "Settings Module Coming Soon"));

        exitBtn = createStyledButton("🚪 Exit");

        exitBtn.setBackground(new Color(198, 40, 40));

        buttonPanel.add(addProductBtn);
        buttonPanel.add(viewProductsBtn);
        buttonPanel.add(searchProductBtn);
        buttonPanel.add(updateProductBtn);
        buttonPanel.add(deleteProductBtn);

        buttonPanel.add(adminManagementBtn);
        buttonPanel.add(supplierManagementBtn);
        buttonPanel.add(employeeManagementBtn);
        buttonPanel.add(purchaseManagementBtn);
        buttonPanel.add(salesBtn);
        buttonPanel.add(reportsBtn);
        buttonPanel.add(settingsBtn);

        buttonPanel.add(exitBtn);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Builds the bottom footer strip with a small credit label. Unchanged.
     */
    private void initFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(DARK_GREEN);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 30));

        JLabel footerLabel = new JLabel("© 2026 Grocery Management System | Final Year Project");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(Color.WHITE);

        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the Logout button click: closes MainDashboard and
     * returns the user to LoginSelectionFrame.
     */
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginSelectionFrame();
            dispose();
        }
    }

    /**
     * Helper method to create a consistently styled JButton. Unchanged.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(BUTTON_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Application entry point for standalone testing of this screen.
     */
    private JLabel createStatCard(String title, String value) {

    	    JLabel label = new JLabel(
    	            "<html><center><b>" + title + "</b><br><br><span style='font-size:24px'>" + value + "</span></center></html>",
    	            SwingConstants.CENTER);

    	    label.setOpaque(true);
    	    label.setBackground(Color.WHITE);
    	    label.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
    	    label.setFont(new Font("SansSerif", Font.BOLD, 15));

    	    return label;
    	}
    private void loadDashboardData() {

        try {

            totalProductsLabel.setText("<html><center><b>Total Products</b><br><br><span style='font-size:24px'>" +
                    productDAO.getTotalProducts() + "</span></center></html>");

            totalCategoriesLabel.setText("<html><center><b>Categories</b><br><br><span style='font-size:24px'>" +
                    productDAO.getTotalCategories() + "</span></center></html>");

            lowStockLabel.setText("<html><center><b>Low Stock</b><br><br><span style='font-size:24px'>" +
                    productDAO.getLowStockCount() + "</span></center></html>");

            outOfStockLabel.setText("<html><center><b>Out Of Stock</b><br><br><span style='font-size:24px'>" +
                    productDAO.getOutOfStockCount() + "</span></center></html>");

            expiredProductsLabel.setText("<html><center><b>Expired</b><br><br><span style='font-size:24px'>" +
                    productDAO.getExpiredProductsCount() + "</span></center></html>");

        } catch (Exception e) {

            e.printStackTrace();

        }

        }   // 👈 ye add karo (loadDashboardData method close)

    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {

            new GuestProductFrame();

        });

    }}// 👈 ye class close karne ke liye