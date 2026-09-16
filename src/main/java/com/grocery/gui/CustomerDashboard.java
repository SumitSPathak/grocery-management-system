package com.grocery.gui;


import com.grocery.model.Customer;
import com.grocery.model.Product;
import com.grocery.model.Order;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.OrderDAO;

import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.dao.impl.OrderDAOImpl;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import java.math.BigDecimal;

import java.net.URI;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
/**
 * CustomerDashboard.java
 * ------------------------------------------------------------------
 * Enterprise-grade dashboard for a logged-in Customer in the Grocery
 * Management System. Follows the exact same architectural pattern as
 * SupplierDashboard.java: top navbar, left profile/navigation sidebar,
 * a CardLayout-driven main content area, and a footer.
 *
 * NOTE ON ASSUMED MODEL/DAO METHOD NAMES:
 * This file assumes the following methods exist (same naming style as
 * your SupplierDAO / OrderDAO). If your actual method names differ,
 * only the marked lines below need renaming — no architecture change.
 *   ProductDAO.getAllProducts() -> List<Product>
 *   ProductDAO.searchProducts(String keyword) -> List<Product>
 *   Product.getProductId(), getProductName(), getPrice(), getStock(), getCategory()
 *   OrderDAO.placeOrder(Order order) -> boolean
 *   OrderDAO.getOrdersByCustomer(int customerId) -> List<Order>
 *   Order: setCustomerId, setProductId, setQuantity, setTotalAmount, setStatus, setOrderDate
 *   Order (for display): getOrderId, getProductId, getQuantity, getTotalAmount, getStatus, getOrderDate
 *   Customer.getCustomerId(), getCustomerName(), getEmail(), getPhone(), getAddress()
 */
public class CustomerDashboard extends JFrame {
	    // ================================================================
    // THEME PALETTE — same green ERP theme used across the project
    // ================================================================
    private static final Color COLOR_PRIMARY_DARK   = new Color(20, 83, 45);
    private static final Color COLOR_PRIMARY         = new Color(34, 110, 62);
    private static final Color COLOR_PRIMARY_LIGHT   = new Color(232, 245, 233);
    private static final Color COLOR_ACCENT           = new Color(255, 179, 0);
    private static final Color COLOR_SIDEBAR_BG       = new Color(23, 43, 33);
    private static final Color COLOR_SIDEBAR_TEXT     = new Color(220, 230, 224);
    private static final Color COLOR_CARD_BG          = Color.WHITE;
    private static final Color COLOR_TEXT_DARK        = new Color(33, 37, 41);
    private static final Color COLOR_TEXT_MUTED        = new Color(120, 130, 125);
    private static final Color COLOR_DANGER            = new Color(198, 40, 40);
    private static final Color COLOR_DANGER_HOVER       = new Color(140, 20, 20);

    private static final Font FONT_BRAND       = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_DATETIME    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SIDEBAR_NAME = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_SIDEBAR_SUB  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_MENU_ITEM    = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_CARD_VALUE   = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_CARD_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SECTION_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_FOOTER        = new Font("Segoe UI", Font.PLAIN, 11);

private JPanel productCardsPanel;
private JTable table;
    // The currently logged-in customer, passed in from CustomerLoginFrame
    private final Customer loggedInCustomer;

    // DAOs — reused across panels, following existing project pattern
    private final ProductDAO productDAO = new ProductDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    // In-memory cart for this session: Product + quantity pairs
    private final List<CartItem> cartItems = new ArrayList<>();

    private JLabel dateTimeLabel;

    private JPanel mainContentPanel;
    private CardLayout mainContentLayout;

    private static final String CARD_HOME     = "HOME";
    private static final String CARD_BROWSE   = "BROWSE";
    private static final String CARD_SEARCH   = "SEARCH";
    private static final String CARD_CART     = "CART";
    private static final String CARD_ORDERS   = "ORDERS";
    private static final String CARD_PROFILE  = "PROFILE";

    private JButton menuHomeBtn;
    private JButton menuBrowseBtn;
    private JButton menuSearchBtn;
    private JButton menuCartBtn;
    private JButton menuOrdersBtn;
    private JButton menuProfileBtn;

    // Labels that need live refresh (dashboard metric cards)
    private JLabel cartCountValueLabel;
    private JLabel totalOrdersValueLabel;
    private JLabel availableProductsValueLabel;

    // Table models kept as fields so panels can be refreshed on demand
    private DefaultTableModel browseTableModel;
    private DefaultTableModel searchTableModel;
    private DefaultTableModel cartTableModel;
    private DefaultTableModel ordersTableModel;
   
    public CustomerDashboard(){

	    this(null);

	}

    public CustomerDashboard(Customer customer) {
        this.loggedInCustomer = customer;

        initializeFrame();
        buildUI();

        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Grocery Management System - Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 760));

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_PRIMARY_LIGHT);
    }

    private void buildUI() {
        add(buildTopNavbar(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(buildLeftSidebar());
        splitPane.setRightComponent(buildMainContentWrapper());
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0);

        add(splitPane, BorderLayout.CENTER);
    }

    // ================================================================
    // TOP NAVBAR
    // ================================================================

    private JPanel buildTopNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(COLOR_PRIMARY_DARK);
        navbar.setBorder(new EmptyBorder(12, 20, 12, 20));
        navbar.setPreferredSize(new Dimension(0, 64));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("🛒");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel brandLabel = new JLabel("Grocery Management System");
        brandLabel.setFont(FONT_BRAND);
        brandLabel.setForeground(Color.WHITE);

        brandPanel.add(logoLabel);
        brandPanel.add(brandLabel);
        navbar.add(brandPanel, BorderLayout.WEST);

        dateTimeLabel = new JLabel("", SwingConstants.CENTER);
        dateTimeLabel.setFont(FONT_DATETIME);
        dateTimeLabel.setForeground(new Color(210, 225, 215));
        navbar.add(dateTimeLabel, BorderLayout.CENTER);
        startLiveClock();

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        String customerName = (loggedInCustomer != null && loggedInCustomer.getCustomerName() != null)
                ? loggedInCustomer.getCustomerName() : "Customer";

        JLabel welcomeLabel = new JLabel("Welcome, " + customerName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);

        JButton logoutBtn = createRoundedButton("Logout", COLOR_DANGER, COLOR_DANGER_HOVER, 90, 34);
        logoutBtn.addActionListener(e -> handleLogout());

        rightPanel.add(welcomeLabel);
        rightPanel.add(logoutBtn);

        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    private void startLiveClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy   •   hh:mm:ss a");
        dateTimeLabel.setText(formatter.format(new Date()));

        Timer clockTimer = new Timer(1000, e -> dateTimeLabel.setText(formatter.format(new Date())));
        clockTimer.start();
    }

    // ================================================================
    // LEFT SIDEBAR
    // ================================================================

    private JPanel buildLeftSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setBorder(new EmptyBorder(30, 20, 20, 20));

        sidebar.add(buildCustomerProfileSection());
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        sidebar.add(buildSidebarSeparator());
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(buildQuickMenuSection());
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel buildCustomerProfileSection() {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setOpaque(false);
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel photoCircle = buildInitialsAvatar();
        photoCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(photoCircle);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 14)));

        String customerName = safeText(loggedInCustomer != null ? loggedInCustomer.getCustomerName() : null, "Customer");
        String phone = safeText(loggedInCustomer != null ? loggedInCustomer.getPhone() : null, "—");
        String email = safeText(loggedInCustomer != null ? loggedInCustomer.getEmail() : null, "—");

        JLabel nameLabel = new JLabel(customerName, SwingConstants.CENTER);
        nameLabel.setFont(FONT_SIDEBAR_NAME);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 12, 0));

        profilePanel.add(nameLabel);
        profilePanel.add(buildSidebarInfoRow("📞", phone));
        profilePanel.add(buildSidebarInfoRow("✉", email));

        return profilePanel;
    }

    private JPanel buildInitialsAvatar() {
        String name = (loggedInCustomer != null && loggedInCustomer.getCustomerName() != null
                && !loggedInCustomer.getCustomerName().isEmpty())
                ? loggedInCustomer.getCustomerName() : "C";
        String initials = String.valueOf(Character.toUpperCase(name.charAt(0)));

        JPanel avatar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(76, 76));
        avatar.setMaximumSize(new Dimension(76, 76));

        JLabel initialsLabel = new JLabel(initials);
        initialsLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        initialsLabel.setForeground(Color.WHITE);
        avatar.add(initialsLabel);

        return avatar;
    }

    private JPanel buildSidebarInfoRow(String icon, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_SIDEBAR_SUB);
        valueLabel.setForeground(COLOR_SIDEBAR_TEXT);

        row.add(iconLabel);
        row.add(valueLabel);
        return row;
    }

    private JSeparator buildSidebarSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(60, 80, 68));
        separator.setBackground(COLOR_SIDEBAR_BG);
        separator.setMaximumSize(new Dimension(230, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }

    private JPanel buildQuickMenuSection() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sectionLabel = new JLabel("QUICK MENU");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        sectionLabel.setForeground(new Color(140, 160, 148));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionLabel.setBorder(new EmptyBorder(0, 4, 10, 0));
        menuPanel.add(sectionLabel);

        menuHomeBtn    = createSidebarMenuButton("🏠  Home", CARD_HOME);
        menuBrowseBtn  = createSidebarMenuButton("🛍  Browse Products", CARD_BROWSE);
        menuSearchBtn  = createSidebarMenuButton("🔍  Search Products", CARD_SEARCH);
        menuCartBtn    = createSidebarMenuButton("🛒  My Cart", CARD_CART);
        menuOrdersBtn  = createSidebarMenuButton("📋  My Orders", CARD_ORDERS);
        menuProfileBtn = createSidebarMenuButton("👤  My Profile", CARD_PROFILE);

        menuPanel.add(menuHomeBtn);
        menuPanel.add(menuBrowseBtn);
        menuPanel.add(menuSearchBtn);
        menuPanel.add(menuCartBtn);
        menuPanel.add(menuOrdersBtn);
        menuPanel.add(menuProfileBtn);

        setActiveMenuButton(menuHomeBtn);

        return menuPanel;
    }

    private JButton createSidebarMenuButton(String text, final String cardName) {
        JButton button = new JButton(text);
        button.setFont(FONT_MENU_ITEM);
        button.setForeground(COLOR_SIDEBAR_TEXT);
        button.setBackground(COLOR_SIDEBAR_BG);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 42));
        button.setBorder(new EmptyBorder(10, 14, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {

            if (CARD_CART.equals(cardName)) {
                refreshCartTable();
            }

            if (CARD_ORDERS.equals(cardName)) {
                refreshOrdersTable();
            }

            if (CARD_HOME.equals(cardName)) {
                refreshHomeMetrics();
            }

            mainContentLayout.show(mainContentPanel, cardName);
            setActiveMenuButton(button);
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(COLOR_PRIMARY)) {
                    button.setBackground(new Color(38, 62, 48));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(COLOR_PRIMARY)) {
                    button.setBackground(COLOR_SIDEBAR_BG);
                }
            }
        });

        return button;
    }

    private void setActiveMenuButton(JButton activeButton) {
        JButton[] allMenuButtons = {
                menuHomeBtn, menuBrowseBtn, menuSearchBtn,
                menuCartBtn, menuOrdersBtn, menuProfileBtn
        };

        for (JButton btn : allMenuButtons) {
            if (btn == null) continue;
            btn.setBackground(btn == activeButton ? COLOR_PRIMARY : COLOR_SIDEBAR_BG);
            btn.setForeground(btn == activeButton ? Color.WHITE : COLOR_SIDEBAR_TEXT);
        }
    }

    private String safeText(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value : fallback;
    }

    // ================================================================
    // MAIN CONTENT AREA (CardLayout host)
    // ================================================================

    private JPanel buildMainContentWrapper() {

        mainContentLayout = new CardLayout();

        mainContentPanel = new JPanel(mainContentLayout);

        mainContentPanel.setBackground(COLOR_PRIMARY_LIGHT);


        mainContentPanel.add(buildHomeCard(), CARD_HOME);

        mainContentPanel.add(buildBrowseProductsPanel(), CARD_BROWSE);

        mainContentPanel.add(buildSearchProductsPanel(), CARD_SEARCH);

        mainContentPanel.add(buildCartPanel(), CARD_CART);

        mainContentPanel.add(buildOrdersPanel(), CARD_ORDERS);

        mainContentPanel.add(buildProfilePanel(), CARD_PROFILE);


        return mainContentPanel;
    }

    // ----------------------------------------------------------------
    // HOME CARD — dashboard metric cards
    // ----------------------------------------------------------------

    private JPanel buildHomeCard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PRIMARY_LIGHT);
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        panel.add(buildMetricCardsSection());
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(buildFooter());

        return panel;
    }

    private JPanel buildMetricCardsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);

        JLabel sectionTitle = new JLabel("Overview");
        sectionTitle.setFont(FONT_SECTION_TITLE);
        sectionTitle.setForeground(COLOR_TEXT_DARK);
        sectionTitle.setBorder(new EmptyBorder(0, 4, 12, 0));
        section.add(sectionTitle, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 18, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.setPreferredSize(new Dimension(0, 140));

        int availableProducts = safeCount(() -> productDAO.getAllProducts());
        int cartCount = cartItems.size();
        int totalOrders = safeCount(() -> orderDAO.getOrdersByCustomer(loggedInCustomer.getCustomerId()));

        JPanel productsCard = buildMetricCard("📦", "Available Products", String.valueOf(availableProducts), COLOR_PRIMARY);
        JPanel cartCard = buildMetricCard("🛒", "Cart Items", String.valueOf(cartCount), new Color(245, 124, 0));
        JPanel ordersCard = buildMetricCard("📋", "Total Orders", String.valueOf(totalOrders), new Color(2, 119, 189));

        availableProductsValueLabel = findValueLabel(productsCard);
        cartCountValueLabel = findValueLabel(cartCard);
        totalOrdersValueLabel = findValueLabel(ordersCard);

        cardsGrid.add(productsCard);
        cardsGrid.add(cartCard);
        cardsGrid.add(ordersCard);

        section.add(cardsGrid, BorderLayout.CENTER);
        return section;
    }

    /** Refreshes the three home metric cards without rebuilding the whole panel. */
    private void refreshHomeMetrics() {
        if (availableProductsValueLabel != null) {
            availableProductsValueLabel.setText(String.valueOf(safeCount(() -> productDAO.getAllProducts())));
        }
        if (cartCountValueLabel != null) {
            cartCountValueLabel.setText(String.valueOf(cartItems.size()));
        }
        if (totalOrdersValueLabel != null) {
            totalOrdersValueLabel.setText(String.valueOf(
                    safeCount(() -> orderDAO.getOrdersByCustomer(loggedInCustomer.getCustomerId()))));
        }
    }

    private interface ListSupplier {
        List<?> get() throws Exception;
    }

    private int safeCount(ListSupplier supplier) {
        try {
            List<?> list = supplier.get();
            return list != null ? list.size() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Walks a metric card's component tree to find its big value JLabel (for live refresh). */
    private JLabel findValueLabel(Container card) {
        for (Component c : card.getComponents()) {
            if (c instanceof Container) {
                if (c instanceof JLabel && ((JLabel) c).getFont().equals(FONT_CARD_VALUE)) {
                    return (JLabel) c;
                }
                JLabel found = findValueLabel((Container) c);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JPanel buildMetricCard(String icon, String label, String value, Color accentColor) {
        final RoundedShadowPanel card = new RoundedShadowPanel(16);
        card.setLayout(new BorderLayout());
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel iconBadge = new RoundedPanel(10, accentColor);
        iconBadge.setPreferredSize(new Dimension(44, 44));
        iconBadge.setMaximumSize(new Dimension(44, 44));
        iconBadge.setLayout(new GridBagLayout());
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconBadge.add(iconLabel);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(iconBadge, BorderLayout.WEST);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_CARD_VALUE);
        valueLabel.setForeground(COLOR_TEXT_DARK);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(FONT_CARD_LABEL);
        labelLabel.setForeground(COLOR_TEXT_MUTED);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(14, 0, 0, 0));
        textPanel.add(valueLabel);
        textPanel.add(labelLabel);

        card.add(topRow, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setHovered(true);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setHovered(false);
                card.repaint();
            }
        });

        return card;
    }

    // ----------------------------------------------------------------
    // BROWSE PRODUCTS PANEL
    // ----------------------------------------------------------------

 private JPanel buildBrowseProductsPanel() {

    JPanel panel =
            new JPanel(new BorderLayout());

    panel.setBackground(
            COLOR_PRIMARY_LIGHT
    );

    panel.setBorder(
            new EmptyBorder(
                    25,
                    30,
                    20,
                    30
            )
    );


    // =========================================================
    // TITLE
    // =========================================================

    JLabel title =
            new JLabel("Browse Products");

    title.setFont(
            FONT_SECTION_TITLE
    );

    title.setForeground(
            COLOR_TEXT_DARK
    );

    title.setBorder(
            new EmptyBorder(
                    0,
                    0,
                    12,
                    0
            )
    );

    panel.add(
            title,
            BorderLayout.NORTH
    );


    // =========================================================
    // NEW PRODUCT VIEW PANEL
    // =========================================================

    ProductViewPanel productViewPanel =
            new ProductViewPanel(
                    product -> {

                        // Add selected product to Customer Cart
                        addProductToCart(
                                product.getProductId(),
                                1
                        );

                    }
            );


    // =========================================================
    // ADD PRODUCT VIEW TO CENTER
    // =========================================================

    panel.add(
            productViewPanel,
            BorderLayout.CENTER
    );


    return panel;
}   /** Loads all products from the database into the Browse Products table. */
    private void refreshBrowseTable() {
        if (browseTableModel == null) return;
        browseTableModel.setRowCount(0);
        try {  

            List<Product> products = productDAO.getAllProducts();  
            int count = 0;
            if (products != null) {  

                for (Product p : products) {  

                    browseTableModel.addRow(new Object[]{  
                            p.getProductId(),  
                            p.getProductName(),  
                            p.getCategory(),  
                            p.getPrice(),  
                            p.getQuantity()
                    });  

                }  
            } 

        } 
        catch(Exception e){ 

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load products: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);}}
            private void loadProductCards() {

                productCardsPanel.removeAll();

                try {

                    List<Product> products = productDAO.getAllProducts();

                    if(products == null || products.isEmpty()) {
                        productCardsPanel.add(new JLabel("No Products Found"));
                        return;
                    }


                    int count = 0;

                    for(Product p : products)
                    {

                        if(count == 30)
                            break;

                        count++;

                        JPanel card = new JPanel();
            card.setLayout(
                    new BorderLayout()
            );


            card.setPreferredSize(
                    new Dimension(220,280)
            );


            card.setBackground(
                    Color.WHITE
            );


            card.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    Color.LIGHT_GRAY
                            ),
                            new EmptyBorder(
                                    15,15,15,15
                            )
                    )
            );



            // ======================
            // PRODUCT IMAGE
            // ======================
         // Product Image Placeholder

            JLabel imageLabel = new JLabel(
                    "🛒",
                    SwingConstants.CENTER
            );

            imageLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            30
                    )
            );


           /* card.add(
                    imageLabel,
                    BorderLayout.NORTH
            );*/


            // ======================
            // PRODUCT NAME
            // ======================

            JLabel nameLabel =
                    new JLabel(
                            p.getProductName(),
                            SwingConstants.CENTER
                    );


            nameLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            16
                    )
            );
            nameLabel.setForeground(Color.BLACK);



            // ======================
            // CATEGORY
            // ======================

            JLabel categoryLabel =
                    new JLabel(
                            p.getCategory(),
                            SwingConstants.CENTER
                    );


            categoryLabel.setForeground(
                    Color.GRAY
            );




            // ======================
            // PRICE
            // ======================

            JLabel priceLabel =
                    new JLabel(
                            "₹ " + p.getPrice(),
                            SwingConstants.CENTER
                    );


            priceLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            18
                    )
            );


            priceLabel.setForeground(
                    new Color(0,128,0)
            );




            // ======================
            // STOCK
            // ======================

            JLabel stockLabel =
                    new JLabel(
                            "Stock: " + p.getQuantity(),
                            SwingConstants.CENTER
                    );





            // ======================
            // ADD CART BUTTON
            // ======================

            JButton addButton =
                    new JButton(
                            "🛒 Add Cart"
                    );


            addButton.addActionListener(e -> {


                addProductToCart(
                        p.getProductId(),
                        1
                );


                JOptionPane.showMessageDialog(
                        this,
                        p.getProductName()
                        + " added to cart"
                );


            });






            JPanel infoPanel =
                    new JPanel(
                            new GridLayout(5,1,5,5)
                    );


            infoPanel.add(nameLabel);

            infoPanel.add(categoryLabel);

            infoPanel.add(priceLabel);

            infoPanel.add(stockLabel);

            infoPanel.add(addButton);




            card.add(
                    infoPanel,
                    BorderLayout.CENTER
            );



            productCardsPanel.add(card);


        }


    }
    catch(Exception e) {


        e.printStackTrace();


        JOptionPane.showMessageDialog(
                this,
                "Unable to load products"
        );

    }



    productCardsPanel.revalidate();

    productCardsPanel.repaint();

}
    
    // ----------------------------------------------------------------
    // SEARCH PRODUCTS PANEL
    // ----------------------------------------------------------------

    private JPanel buildSearchProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY_LIGHT);
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("Search Products");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox.setOpaque(false);
        JTextField searchField = new JTextField(20);
        JButton searchBtn = createRoundedButton("Search", COLOR_PRIMARY, COLOR_PRIMARY_DARK, 100, 34);

        searchTableModel = new DefaultTableModel(
                new String[]{"ID", "Product Name", "Category", "Price (₹)", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
      //  table.setRowHeight(30);
       // table.setFont(FONT_CARD_LABEL);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            searchTableModel.setRowCount(0);
            try {
                // ASSUMPTION: ProductDAO has searchProducts(String keyword).
                // If your method name differs, rename this one call only.
                List<Product> results = productDAO.searchProducts(keyword);
                if (results != null) {
                    for (Product p : results) {
                        searchTableModel.addRow(new Object[]{
                                p.getProductId(), p.getProductName(), p.getCategory(),
                                p.getPrice(), p.getQuantity()
                        });
                    }
                }
                if (results == null || results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No products matched your search.",
                            "No Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Search failed: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchBox.add(searchField);
        searchBox.add(searchBtn);
        topBar.add(searchBox, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);
        table = new JTable(searchTableModel);
        table.setRowHeight(30);
        table.setFont(FONT_CARD_LABEL);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setOpaque(false);
        JTextField qtyField = new JTextField("1", 4);
        JButton addToCartBtn = createRoundedButton("Add Selected to Cart", COLOR_PRIMARY,
                COLOR_PRIMARY_DARK, 200, 36);

        addToCartBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product first.",
                        "No Product Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int quantity = Integer.parseInt(qtyField.getText().trim());
                int productId = (int) searchTableModel.getValueAt(selectedRow, 0);
                addProductToCart(productId, quantity);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.",
                        "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomBar.add(new JLabel("Qty:"));
        bottomBar.add(qtyField);
        bottomBar.add(addToCartBtn);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    // ----------------------------------------------------------------
    // CART PANEL
    // ----------------------------------------------------------------

    /** Simple in-memory cart line item: a Product plus chosen quantity. */
    private static class CartItem {
        final Product product;
        int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    /** Adds a product (by ID) to the session cart, merging quantity if already present. */
    private void addProductToCart(int productId, int quantity) {
        try {
            List<Product> allProducts = productDAO.getAllProducts();
            Product target = null;
            if (allProducts != null) {
                for (Product p : allProducts) {
                    if (p.getProductId() == productId) {
                        target = p;
                        break;
                    }
                }
            }
            if (target == null) {
                JOptionPane.showMessageDialog(this, "Product not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean merged = false;
            for (CartItem item : cartItems) {
                if (item.product.getProductId() == productId) {
                    item.quantity += quantity;
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                cartItems.add(new CartItem(target, quantity));
            }

            JOptionPane.showMessageDialog(this, target.getProductName() + " added to cart.",
                    "Added to Cart", JOptionPane.INFORMATION_MESSAGE);

            refreshHomeMetrics();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not add product to cart: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY_LIGHT);
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel title = new JLabel("My Cart");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(title, BorderLayout.NORTH);

        cartTableModel = new DefaultTableModel(
                new String[]{"Product ID", "Product Name", "Price (₹)", "Quantity", "Subtotal (₹)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(cartTableModel);
        table.setRowHeight(30);
        table.setFont(FONT_CARD_LABEL);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomBar.add(totalLabel, BorderLayout.WEST);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionButtons.setOpaque(false);

        JButton removeBtn = createRoundedButton("Remove Selected", COLOR_DANGER, COLOR_DANGER_HOVER, 150, 34);
        removeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.",
                        "No Item Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cartItems.remove(selectedRow);
            refreshCartTable();
            refreshHomeMetrics();
        });

        JButton placeOrderBtn = createRoundedButton("Place Order", COLOR_PRIMARY, COLOR_PRIMARY_DARK, 150, 34);
        placeOrderBtn.addActionListener(e -> placeOrder());

        actionButtons.add(removeBtn);
        actionButtons.add(placeOrderBtn);
        bottomBar.add(actionButtons, BorderLayout.EAST);

        panel.add(bottomBar, BorderLayout.SOUTH);

        // Recalculate the total label whenever the cart table refreshes
        cartTableModel.addTableModelListener(e -> {
        	BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cartItems) {

                total = total.add(
                        item.product.getPrice()
                        .multiply(
                                new java.math.BigDecimal(item.quantity)
                        )
                );

            }
            totalLabel.setText(String.format("Total: ₹%.2f", total));
        });

        return panel;
    }

    private void refreshCartTable() {
        if (cartTableModel == null) return;
        cartTableModel.setRowCount(0);
        for (CartItem item : cartItems) {
            BigDecimal subtotal =item.product.getPrice()
            		.multiply(
            			    new java.math.BigDecimal(item.quantity)
            			);
            cartTableModel.addRow(new Object[]{
                    item.product.getProductId(), item.product.getProductName(),
                    item.product.getPrice(), item.quantity, subtotal
            });
        }
    }

    private void placeOrder() {

        if (cartItems == null || cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Your cart is empty.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (loggedInCustomer == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please login as a customer before placing an order.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String location = getDeliveryLocation();

        if (location == null || location.trim().isEmpty()) {
            return;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            totalAmount = totalAmount.add(
                    item.product.getPrice().multiply(
                            BigDecimal.valueOf(item.quantity)
                    )
            );
        }

        boolean paymentSuccessful =
                showUPIPaymentDialog(totalAmount, location);

        if (!paymentSuccessful) {
            return;
        }

        try {
            for (CartItem item : cartItems) {

                Order order = new Order();

                order.setProductId(item.product.getProductId());
                order.setQuantity(item.quantity);

                order.setTotalAmount(
                        item.product.getPrice().multiply(
                                BigDecimal.valueOf(item.quantity)
                        )
                );

                order.setStatus("PENDING");

                order.setOrderDate(
                        new java.sql.Timestamp(
                                System.currentTimeMillis()
                        )
                );

                if (!orderDAO.addOrder(order)) {
                    throw new Exception(
                            "Order failed for product: "
                                    + item.product.getProductName()
                    );
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Order placed successfully!\n\n"
                            + "Payment: ₹" + totalAmount + "\n"
                            + "Paid to: sumitspathak@upi\n\n"
                            + "Delivery Location:\n" + location,
                    "Order Placed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cartItems.clear();
            refreshCartTable();
            refreshOrdersTable();
            refreshHomeMetrics();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not place order:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String getDeliveryLocation() {

        int permission = JOptionPane.showConfirmDialog(
                this,
                "Allow Grocery to collect your delivery location?",
                "Location Permission",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (permission != JOptionPane.YES_OPTION) {
            return null;
        }

        String location = JOptionPane.showInputDialog(
                this,
                "Enter your delivery address/location:",
                "Delivery Location",
                JOptionPane.QUESTION_MESSAGE
        );

        if (location == null || location.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid delivery location.",
                    "Invalid Location",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        return location.trim();
    }

    private boolean showUPIPaymentDialog(
            BigDecimal amount,
            String location
    ) {

        final String UPI_ID = "sumitspathak@upi";

        String amountText = amount
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .toPlainString();

        String upiUrl =
                "upi://pay"
                        + "?pa=" + URLEncoder.encode(
                                UPI_ID, StandardCharsets.UTF_8)
                        + "&pn=" + URLEncoder.encode(
                                "Grocery Management System",
                                StandardCharsets.UTF_8)
                        + "&am=" + amountText
                        + "&cu=INR"
                        + "&tn=" + URLEncoder.encode(
                                "Grocery Order Payment",
                                StandardCharsets.UTF_8);

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel title = new JLabel(
                "Grocery Payment",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        mainPanel.add(title, BorderLayout.NORTH);

        JLabel qrLabel = new JLabel(
                "Generating QR...",
                SwingConstants.CENTER
        );

        qrLabel.setPreferredSize(
                new Dimension(300, 300)
        );

        try {

            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    upiUrl,
                    BarcodeFormat.QR_CODE,
                    280,
                    280
            );

            BufferedImage qrImage =
                    MatrixToImageWriter.toBufferedImage(matrix);

            qrLabel.setText("");
            qrLabel.setIcon(new ImageIcon(qrImage));

        } catch (Exception ex) {

            qrLabel.setText(
                    "<html><center>"
                            + "<b>QR generation failed</b><br>"
                            + "UPI ID: " + UPI_ID
                            + "<br>Amount: ₹" + amountText
                            + "</center></html>"
            );

            ex.printStackTrace();
        }

        mainPanel.add(qrLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(
                new BoxLayout(infoPanel, BoxLayout.Y_AXIS)
        );

        JLabel amountLabel = new JLabel(
                "Amount: ₹" + amountText,
                SwingConstants.CENTER
        );
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        JLabel upiLabel = new JLabel(
                "UPI ID: " + UPI_ID,
                SwingConstants.CENTER
        );
        upiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        upiLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 16)
        );

        JLabel locationLabel = new JLabel(
                "<html><center><b>Delivery:</b><br>"
                        + escapeHtml(location)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruction = new JLabel(
                "Scan this QR using Google Pay, PhonePe or Paytm.",
                SwingConstants.CENTER
        );
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(amountLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(upiLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(instruction);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5)
        );

        JButton copyButton = new JButton("Copy UPI ID");

        copyButton.addActionListener(e -> {

            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(
                            new StringSelection(UPI_ID),
                            null
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "UPI ID copied:\n" + UPI_ID,
                    "Copied",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JButton openButton = new JButton(
                "Open UPI Payment"
        );

        openButton.addActionListener(e -> {

            try {

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(
                            new URI(upiUrl)
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please scan the QR code.",
                        "UPI Payment",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        buttonPanel.add(copyButton);
        buttonPanel.add(openButton);

        final boolean[] completed = {false};

        JButton paidButton = new JButton(
                "Payment Completed"
        );

        JDialog dialog = new JDialog(
                this,
                "Complete Payment",
                true
        );

        paidButton.addActionListener(e -> {

            completed[0] = true;

            JOptionPane.showMessageDialog(
                    dialog,
                    "Payment marked as completed.\n\n"
                            + "Amount: ₹" + amountText
                            + "\nPaid to: " + UPI_ID,
                    "Payment Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dialog.dispose();
        });

        JPanel bottomPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        bottomPanel.add(
                infoPanel,
                BorderLayout.NORTH
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                paidButton,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(mainPanel);
        dialog.setSize(500, 700);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return completed[0];
    }

    private String escapeHtml(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "Not provided";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // ----------------------------------------------------------------
    // ORDERS PANEL
    // ----------------------------------------------------------------

    private JPanel buildOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY_LIGHT);
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel title = new JLabel("My Orders");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(title, BorderLayout.NORTH);

        ordersTableModel = new DefaultTableModel(
                new String[]{"Order ID", "Product ID", "Quantity", "Total (₹)", "Status", "Order Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(ordersTableModel);
        table.setRowHeight(30);
        table.setFont(FONT_CARD_LABEL);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshOrdersTable();

        return panel;
    }

    /** Loads only this customer's own orders — never all orders. */
    private void refreshOrdersTable() {
        if (ordersTableModel == null) return;
        ordersTableModel.setRowCount(0);
        try {
            // ASSUMPTION: OrderDAO has getOrdersByCustomer(int customerId),
            // mirroring the getOrdersBySupplier(int) pattern used elsewhere.
            List<Order> orders = orderDAO.getOrdersByCustomer(loggedInCustomer.getCustomerId());
            if (orders != null) {
                for (Order o : orders) {
                    ordersTableModel.addRow(new Object[]{
                            o.getOrderId(), o.getProductId(), o.getQuantity(),
                            o.getTotalAmount(), o.getStatus(), o.getOrderDate()
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not load your orders: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------------------------------------------
    // PROFILE PANEL
    // ----------------------------------------------------------------

    private JPanel buildProfilePanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(COLOR_PRIMARY_LIGHT);

        RoundedShadowPanel card = new RoundedShadowPanel(16);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new EmptyBorder(24, 30, 24, 30));
        card.setPreferredSize(new Dimension(420, 260));

        JLabel title = new JLabel("My Profile");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));

        card.add(title);

        card.add(buildProfileRow("Name", loggedInCustomer.getCustomerName()));
        card.add(buildProfileRow("Email", loggedInCustomer.getEmail()));
        card.add(buildProfileRow("Phone", loggedInCustomer.getPhone()));
        card.add(buildProfileRow("Address", loggedInCustomer.getAddress()));

        panel.add(card);

        return panel;
    }
    private JPanel buildProfileRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelLabel = new JLabel(label + ":");
        labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelLabel.setForeground(COLOR_TEXT_MUTED);
        labelLabel.setPreferredSize(new Dimension(90, 20));

        JLabel valueLabel = new JLabel(safeText(value, "—"));
        valueLabel.setFont(FONT_CARD_LABEL);
        valueLabel.setForeground(COLOR_TEXT_DARK);

        row.add(labelLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    // ================================================================
    // FOOTER
    // ================================================================

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 4, 4, 4));

        JLabel versionLabel = new JLabel("Grocery Management System  v1.0.0");
        versionLabel.setFont(FONT_FOOTER);
        versionLabel.setForeground(COLOR_TEXT_MUTED);

        JLabel copyrightLabel = new JLabel("© 2026 Grocery Management System — Customer Portal. All rights reserved.");
        copyrightLabel.setFont(FONT_FOOTER);
        copyrightLabel.setForeground(COLOR_TEXT_MUTED);
        copyrightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        footer.add(versionLabel, BorderLayout.WEST);
        footer.add(copyrightLabel, BorderLayout.EAST);

        return footer;
    }

    // ================================================================
    // ROUNDED UI HELPER COMPONENTS (same as SupplierDashboard)
    // ================================================================

    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color fillColor;

        RoundedPanel(int cornerRadius, Color fillColor) {
            this.cornerRadius = cornerRadius;
            this.fillColor = fillColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedShadowPanel extends JPanel {
        private final int cornerRadius;
        private boolean hovered = false;

        RoundedShadowPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        void setHovered(boolean hovered) {
            this.hovered = hovered;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowOffset = hovered ? 6 : 3;
            int shadowAlpha = hovered ? 70 : 35;

            g2.setColor(new Color(0, 0, 0, shadowAlpha));
            g2.fill(new RoundRectangle2D.Float(
                    shadowOffset, shadowOffset,
                    getWidth() - shadowOffset, getHeight() - shadowOffset,
                    cornerRadius, cornerRadius));

            int liftOffset = hovered ? 2 : 0;
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(
                    0, -liftOffset,
                    getWidth() - shadowOffset, getHeight() - shadowOffset,
                    cornerRadius, cornerRadius));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JButton createRoundedButton(String text, final Color baseColor, final Color hoverColor,
                                         int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(width, height));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // ================================================================
    // LOGOUT
    // ================================================================

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginSelectionFrame();
        }
    }
}