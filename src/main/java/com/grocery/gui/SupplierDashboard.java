package com.grocery.gui;

import com.grocery.model.Supplier;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SupplierDashboard.java
 * ------------------------------------------------------------------
 * Enterprise-grade dashboard for a logged-in Supplier in the Grocery
 * ERP System. Modeled after modern ERP UIs (Zoho Inventory / Tally
 * style): top navbar, left profile/navigation sidebar, a card-based
 * main content area, a recent-activity feed, a notifications panel,
 * and a footer.
 *
 * Architecture notes:
 *  - All dashboard metric cards (Total Products, Pending Orders, etc.)
 *    are built via loadDashboardMetrics(), which currently returns
 *    placeholder values but is structured so a future step can swap
 *    the placeholder logic for real SupplierDAO / OrderDAO queries
 *    without touching any layout code.
 *  - Sidebar "Quick Menu" buttons switch the main content area via a
 *    CardLayout, so future feature screens (Products, Orders, etc.)
 *    can be added as new cards without rebuilding the frame.
 *
 * This class belongs to the GUI layer only — no direct JDBC calls are
 * made here beyond what a future DAO integration will provide through
 * the clearly marked "future-ready" methods.
 */
public class SupplierDashboard extends JFrame {

    // ================================================================
    // THEME PALETTE — professional ERP-style greens, grays, and accents
    // ================================================================
    private static final Color COLOR_PRIMARY_DARK   = new Color(20, 83, 45);   // Deep enterprise green
    private static final Color COLOR_PRIMARY         = new Color(34, 110, 62);  // Primary green
    private static final Color COLOR_PRIMARY_LIGHT   = new Color(232, 245, 233);// Light green background
    private static final Color COLOR_ACCENT           = new Color(255, 179, 0); // Amber accent (notifications)
    private static final Color COLOR_SIDEBAR_BG       = new Color(23, 43, 33);  // Near-black green sidebar
    private static final Color COLOR_SIDEBAR_TEXT     = new Color(220, 230, 224);
    private static final Color COLOR_CARD_BG          = Color.WHITE;
    private static final Color COLOR_CARD_SHADOW      = new Color(0, 0, 0, 35);
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

    // The currently logged-in supplier, passed in from SupplierLoginFrame
    private final Supplier loggedInSupplier;

    // Live clock label in the navbar, updated every second
    private JLabel dateTimeLabel;

    // Main content area swaps panels via CardLayout as sidebar menu
    // items are clicked (Dashboard / Products / Orders / etc.)
    private JPanel mainContentPanel;
    private CardLayout mainContentLayout;

    // Card name constants used with CardLayout.show(...)
    private static final String CARD_DASHBOARD  = "DASHBOARD";
    private static final String CARD_PRODUCTS   = "PRODUCTS";
    private static final String CARD_ORDERS     = "ORDERS";
    private static final String CARD_DELIVERIES = "DELIVERIES";
    private static final String CARD_PAYMENTS   = "PAYMENTS";
    private static final String CARD_HISTORY    = "HISTORY";

    // Sidebar quick-menu buttons kept as fields so their selected state
    // can be visually updated when clicked.
    private JButton menuDashboardBtn;
    private JButton menuProductsBtn;
    private JButton menuOrdersBtn;
    private JButton menuDeliveriesBtn;
    private JButton menuPaymentsBtn;
    private JButton menuHistoryBtn;

    /**
     * Constructs and displays the enterprise Supplier Dashboard for the
     * given logged-in supplier.
     *
     * @param supplier the Supplier who just successfully logged in
     */
  
    /**
     * Configures the base JFrame settings: title, full-screen behaviour,
     * close operation, and centers the window on screen.
     */
    public SupplierDashboard(Supplier supplier) {
        this.loggedInSupplier = supplier;

        initializeFrame();
        buildUI();

        setVisible(true);
    }
    private void initializeFrame() {
        setTitle("Grocery ERP System - Supplier Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 760));

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_PRIMARY_LIGHT);
    }

    /**
     * Assembles the full UI: top navbar (NORTH), a JSplitPane dividing
     * the left sidebar from the main content area (CENTER).
     */
    private void buildUI() {
        add(buildTopNavbar(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(buildLeftSidebar());
        splitPane.setRightComponent(buildMainContentWrapper());
        splitPane.setDividerSize(0);          // sidebar width is fixed via preferred size, no drag handle
        splitPane.setEnabled(false);           // prevents the user from dragging the divider
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0);          // extra space always goes to the right (main content)

        add(splitPane, BorderLayout.CENTER);
    }

    // ================================================================
    // TOP NAVBAR
    // ================================================================

    /**
     * Builds the top navigation bar: brand/logo on the left, live
     * date-time in the middle, and notification icon + supplier name +
     * logout button on the right.
     *
     * @return the fully configured navbar JPanel
     */
    private JPanel buildTopNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(COLOR_PRIMARY_DARK);
        navbar.setBorder(new EmptyBorder(12, 20, 12, 20));
        navbar.setPreferredSize(new Dimension(0, 64));

        // ---- Left: logo + brand name ----
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("🛒");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel brandLabel = new JLabel("Grocery ERP System");
        brandLabel.setFont(FONT_BRAND);
        brandLabel.setForeground(Color.WHITE);

        brandPanel.add(logoLabel);
        brandPanel.add(brandLabel);
        navbar.add(brandPanel, BorderLayout.WEST);

        // ---- Center: live date & time ----
        dateTimeLabel = new JLabel("", SwingConstants.CENTER);
        dateTimeLabel.setFont(FONT_DATETIME);
        dateTimeLabel.setForeground(new Color(210, 225, 215));
        navbar.add(dateTimeLabel, BorderLayout.CENTER);
        startLiveClock();

        // ---- Right: notification icon + supplier name + logout ----
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel notificationIcon = new JLabel("🔔");
        notificationIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        notificationIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notificationIcon.setToolTipText("Notifications");
        notificationIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(SupplierDashboard.this,
                        "You have new alerts. See the Notifications panel below.",
                        "Notifications", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        String supplierName = (loggedInSupplier != null && loggedInSupplier.getSupplierName() != null)
                ? loggedInSupplier.getSupplierName() : "Supplier";

        JLabel supplierNameLabel = new JLabel(supplierName);
        supplierNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        supplierNameLabel.setForeground(Color.WHITE);

        JButton logoutBtn = createRoundedButton("Logout", COLOR_DANGER, COLOR_DANGER_HOVER, 90, 34);
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        rightPanel.add(notificationIcon);
        rightPanel.add(supplierNameLabel);
        rightPanel.add(logoutBtn);

        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    /**
     * Starts a Swing Timer that updates {@link #dateTimeLabel} with the
     * current date and time every second, giving the navbar a live clock.
     */
    private void startLiveClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy   •   hh:mm:ss a");

        // Set the initial value immediately so there's no blank flash
        dateTimeLabel.setText(formatter.format(new Date()));

        Timer clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateTimeLabel.setText(formatter.format(new Date()));
            }
        });
        clockTimer.start();
    }

    // ================================================================
    // LEFT SIDEBAR
    // ================================================================

    /**
     * Builds the left sidebar: supplier photo placeholder, identity
     * details (name, company, phone, email, GST, status), and the
     * Quick Menu navigation buttons.
     *
     * @return the fully configured sidebar JPanel
     */
    private JPanel buildLeftSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setBorder(new EmptyBorder(30, 20, 20, 20));

        sidebar.add(buildSupplierProfileSection());
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        sidebar.add(buildSidebarSeparator());
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(buildQuickMenuSection());
        sidebar.add(Box.createVerticalGlue()); // pushes everything above to the top

        return sidebar;
    }

    /**
     * Builds the supplier identity block at the top of the sidebar:
     * a circular photo placeholder plus name/company/phone/email/GST/status.
     *
     * @return the fully configured profile JPanel
     */
    private JPanel buildSupplierProfileSection() {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setOpaque(false);
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Circular photo placeholder (initials avatar, since no image
        // upload feature exists yet)
        JPanel photoCircle = buildInitialsAvatar();
        photoCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(photoCircle);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 14)));

        String supplierName = safeText(loggedInSupplier != null ? loggedInSupplier.getSupplierName() : null, "Supplier");
        String companyName = safeText(loggedInSupplier != null ? loggedInSupplier.getCompanyName() : null, "—");
        String phone = safeText(loggedInSupplier != null ? loggedInSupplier.getPhone() : null, "—");
        String email = safeText(loggedInSupplier != null ? loggedInSupplier.getEmail() : null, "—");
        String gstNumber = safeText(loggedInSupplier != null ? loggedInSupplier.getGstNumber() : null, "—");
        String status = safeText(loggedInSupplier != null ? loggedInSupplier.getStatus() : null, "ACTIVE");

        JLabel nameLabel = new JLabel(supplierName, SwingConstants.CENTER);
        nameLabel.setFont(FONT_SIDEBAR_NAME);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel companyLabel = new JLabel(companyName, SwingConstants.CENTER);
        companyLabel.setFont(FONT_SIDEBAR_SUB);
        companyLabel.setForeground(new Color(180, 200, 188));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        companyLabel.setBorder(new EmptyBorder(2, 0, 12, 0));

        profilePanel.add(nameLabel);
        profilePanel.add(companyLabel);

        profilePanel.add(buildSidebarInfoRow("📞", phone));
        profilePanel.add(buildSidebarInfoRow("✉", email));
        profilePanel.add(buildSidebarInfoRow("🧾", gstNumber));
        profilePanel.add(buildSidebarStatusBadge(status));

        return profilePanel;
    }

    /**
     * Builds a circular avatar containing the supplier's initials, used
     * as a stand-in for an uploaded photo (no photo-upload feature
     * exists yet in this module).
     *
     * @return a fixed-size circular JPanel with centered initials
     */
    private JPanel buildInitialsAvatar() {
        String name = (loggedInSupplier != null && loggedInSupplier.getSupplierName() != null
                && !loggedInSupplier.getSupplierName().isEmpty())
                ? loggedInSupplier.getSupplierName() : "S";
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

    /**
     * Builds a single icon + text row used for phone/email/GST display
     * in the sidebar profile section.
     *
     * @param icon  an emoji/text icon shown before the value
     * @param value the value text to display
     * @return a fully configured JPanel row
     */
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

    /**
     * Builds a small rounded status badge (e.g. "ACTIVE") shown below
     * the supplier's contact details in the sidebar.
     *
     * @param status the supplier's status string (e.g. "ACTIVE", "INACTIVE")
     * @return a fully configured rounded badge JPanel
     */
    private JPanel buildSidebarStatusBadge(String status) {
        boolean isActive = status != null && status.equalsIgnoreCase("ACTIVE");
        Color badgeColor = isActive ? new Color(56, 142, 60) : new Color(158, 158, 158);

        JPanel badge = new RoundedPanel(14, badgeColor);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 4));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(new EmptyBorder(2, 14, 2, 14));
        badge.setMaximumSize(new Dimension(120, 26));

        JLabel statusLabel = new JLabel(status != null ? status.toUpperCase() : "ACTIVE");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(Color.WHITE);

        badge.add(statusLabel);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(10, 0, 0, 0));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(badge);
        return wrapper;
    }

    /**
     * Builds a thin horizontal separator line used to divide sections
     * of the sidebar.
     *
     * @return a configured JSeparator
     */
    private JSeparator buildSidebarSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(60, 80, 68));
        separator.setBackground(COLOR_SIDEBAR_BG);
        separator.setMaximumSize(new Dimension(230, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }

    /**
     * Builds the "Quick Menu" navigation section of the sidebar:
     * Dashboard, Products, Orders, Deliveries, Payments, History.
     * Each button switches the main content CardLayout when clicked.
     *
     * @return the fully configured quick-menu JPanel
     */
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

        menuDashboardBtn  = createSidebarMenuButton("🏠  Dashboard", CARD_DASHBOARD);
        menuProductsBtn   = createSidebarMenuButton("📦  Products", CARD_PRODUCTS);
        menuOrdersBtn      = createSidebarMenuButton("📋  Orders", CARD_ORDERS);
        menuDeliveriesBtn  = createSidebarMenuButton("🚚  Deliveries", CARD_DELIVERIES);
        menuPaymentsBtn    = createSidebarMenuButton("💳  Payments", CARD_PAYMENTS);
        menuHistoryBtn      = createSidebarMenuButton("📜  History", CARD_HISTORY);

        menuPanel.add(menuDashboardBtn);
        menuPanel.add(menuProductsBtn);
        menuPanel.add(menuOrdersBtn);
        menuPanel.add(menuDeliveriesBtn);
        menuPanel.add(menuPaymentsBtn);
        menuPanel.add(menuHistoryBtn);

        // Mark Dashboard as the initially selected menu item
        setActiveMenuButton(menuDashboardBtn);

        return menuPanel;
    }

    /**
     * Creates a single left-aligned sidebar menu button that, when
     * clicked, switches the main content area to the given card and
     * visually highlights itself as the active menu item.
     *
     * @param text     the button label (with icon)
     * @param cardName the CardLayout card name to switch to on click
     * @return a fully configured JButton
     */
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

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContentLayout.show(mainContentPanel, cardName);
                setActiveMenuButton(button);
            }
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

    /**
     * Visually marks the given sidebar menu button as the active/selected
     * item (green highlight) and resets all other quick-menu buttons back
     * to their default background.
     *
     * @param activeButton the button that was just clicked
     */
    private void setActiveMenuButton(JButton activeButton) {
        JButton[] allMenuButtons = {
                menuDashboardBtn, menuProductsBtn, menuOrdersBtn,
                menuDeliveriesBtn, menuPaymentsBtn, menuHistoryBtn
        };

        for (JButton btn : allMenuButtons) {
            if (btn == null) continue; // guards the very first call during construction
            btn.setBackground(btn == activeButton ? COLOR_PRIMARY : COLOR_SIDEBAR_BG);
            btn.setForeground(btn == activeButton ? Color.WHITE : COLOR_SIDEBAR_TEXT);
        }
    }

    /**
     * Returns the given value if it is non-null and non-empty,
     * otherwise returns the provided fallback text. Keeps the sidebar
     * from displaying "null" for any missing supplier field.
     */
    private String safeText(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value : fallback;
    }
 // ================================================================
    // MAIN CONTENT AREA (CardLayout host)
    // ================================================================

    /**
     * Wraps the main content area (which uses CardLayout to switch
     * between Dashboard / Products / Orders / Deliveries / Payments /
     * History) inside a scroll pane so tall content never gets clipped
     * on smaller screens.
     *
     * @return the fully configured main content wrapper JPanel
     */
    private JScrollPane buildMainContentWrapper() {
        mainContentLayout = new CardLayout();
        mainContentPanel = new JPanel(mainContentLayout);
        mainContentPanel.setBackground(COLOR_PRIMARY_LIGHT);

        mainContentPanel.add(buildDashboardCard(), CARD_DASHBOARD);
        mainContentPanel.add(buildProductsPanel(), CARD_PRODUCTS);
        mainContentPanel.add(buildOrdersPanel(), CARD_ORDERS);;
        mainContentPanel.add(buildPlaceholderCard("Deliveries", "🚚"), CARD_DELIVERIES);
        mainContentPanel.add(buildPlaceholderCard("Payments", "💳"), CARD_PAYMENTS);
        mainContentPanel.add(buildPlaceholderCard("History", "📜"), CARD_HISTORY);

        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    /**
     * Builds the main "Dashboard" card: metric cards grid, recent
     * activity section, notifications panel, and footer — everything
     * the supplier sees by default when the dashboard opens.
     *
     * @return the fully configured Dashboard content JPanel
     */
    private JPanel buildDashboardCard() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(COLOR_PRIMARY_LIGHT);
        dashboardPanel.setBorder(new EmptyBorder(25, 30, 20, 30));

        dashboardPanel.add(buildMetricCardsSection());
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        dashboardPanel.add(buildRecentActivityAndNotificationsSection());
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dashboardPanel.add(buildFooter());

        return dashboardPanel;
    }

    /**
     * Builds a simple placeholder card for sidebar menu items whose
     * real screens (Products, Orders, Deliveries, Payments, History)
     * have not been implemented yet.
     *
     * @param featureName the feature's display name
     * @param icon        an emoji icon representing the feature
     * @return a fully configured placeholder JPanel
     */
    private JPanel buildPlaceholderCard(String featureName, String icon) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PRIMARY_LIGHT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(featureName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel subLabel = new JLabel("This module will be connected to MySQL in a future step.", SwingConstants.CENTER);
        subLabel.setFont(FONT_CARD_LABEL);
        subLabel.setForeground(COLOR_TEXT_MUTED);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(iconLabel);
        content.add(titleLabel);
        content.add(subLabel);

        panel.add(content);
        return panel;
    }

    // ================================================================
    // DASHBOARD METRIC CARDS
    // ================================================================

    /**
     * Builds the grid of dashboard metric cards (Total Products,
     * Pending Orders, Completed Orders, Today's Deliveries, Payments
     * Received, Supplier Rating). Uses GridLayout so all cards resize
     * evenly and responsively as the window resizes — no fixed widths.
     *
     * @return the fully configured metric cards section JPanel
     */
    private JPanel buildMetricCardsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);

        JLabel sectionTitle = new JLabel("Overview");
        sectionTitle.setFont(FONT_SECTION_TITLE);
        sectionTitle.setForeground(COLOR_TEXT_DARK);
        sectionTitle.setBorder(new EmptyBorder(0, 4, 12, 0));
        section.add(sectionTitle, BorderLayout.NORTH);

        // GridLayout with 0 fixed rows lets the layout wrap responsively;
        // 6 columns with equal weight means every card resizes together
        // and there is never leftover empty space.
        JPanel cardsGrid = new JPanel(new GridLayout(1, 6, 18, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.setPreferredSize(new Dimension(0, 140));

        // Pulls current metric values — placeholder now, DB-backed later
        DashboardMetrics metrics = loadDashboardMetrics();

        cardsGrid.add(buildMetricCard("📦", "Total Products", String.valueOf(metrics.totalProducts), COLOR_PRIMARY));
        cardsGrid.add(buildMetricCard("⏳", "Pending Orders", String.valueOf(metrics.pendingOrders), new Color(245, 124, 0)));
        cardsGrid.add(buildMetricCard("✅", "Completed Orders", String.valueOf(metrics.completedOrders), new Color(46, 125, 50)));
        cardsGrid.add(buildMetricCard("🚚", "Today's Deliveries", String.valueOf(metrics.todaysDeliveries), new Color(2, 119, 189)));
        cardsGrid.add(buildMetricCard("💰", "Payments Received", metrics.paymentsReceivedDisplay, new Color(0, 137, 123)));
        cardsGrid.add(buildMetricCard("⭐", "Supplier Rating", metrics.supplierRatingDisplay, new Color(255, 143, 0)));

        section.add(cardsGrid, BorderLayout.CENTER);
        return section;
    }

    /**
     * Builds a single dashboard metric card: large icon, big value,
     * label, rounded corners, a soft drop shadow, and a hover animation
     * that lifts the card slightly and deepens its shadow.
     *
     * @param icon      an emoji icon representing the metric
     * @param label     the metric's display label (e.g. "Total Products")
     * @param value     the metric's current value, pre-formatted as text
     * @param accentColor a color used for the icon badge and left accent bar
     * @return a fully configured, self-contained metric card JPanel
     */
    private JPanel buildMetricCard(String icon, String label, String value, Color accentColor) {
        final RoundedShadowPanel card = new RoundedShadowPanel(16);
        card.setLayout(new BorderLayout());
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon badge (small rounded square with the accent color)
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

        // Hover animation: lift the card (via increased shadow offset)
        // and slightly brighten the background on mouse-over.
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

    /**
     * Simple data holder for dashboard metric values.
     *
     * FUTURE-READY: Every field here is currently populated with
     * placeholder data inside {@link #loadDashboardMetrics()}. When
     * SupplierDAO / OrderDAO / PaymentDAO methods for these metrics
     * are added, only loadDashboardMetrics() needs to change — no
     * layout or card-building code needs to be touched.
     */
    private static class DashboardMetrics {
        int totalProducts;
        int pendingOrders;
        int completedOrders;
        int todaysDeliveries;
        String paymentsReceivedDisplay;
        String supplierRatingDisplay;
    }

    /**
     * Loads the values shown on the dashboard metric cards.
     *
     * FUTURE-READY: This method currently returns hardcoded placeholder
     * values. It is intentionally isolated from all UI-building code so
     * that a future step can replace its body with real calls such as:
     *   productDAO.getTotalProductsBySupplier(supplierId)
     *   orderDAO.getPendingOrderCount(supplierId)
     *   orderDAO.getCompletedOrderCount(supplierId)
     *   deliveryDAO.getTodaysDeliveryCount(supplierId)
     *   paymentDAO.getTotalPaymentsReceived(supplierId)
     *   ratingDAO.getSupplierRating(supplierId)
     * without changing buildMetricCardsSection() or buildMetricCard().
     *
     * @return a populated DashboardMetrics object
     */
    private DashboardMetrics loadDashboardMetrics() {
        DashboardMetrics metrics = new DashboardMetrics();

        // TODO: Replace placeholder values with real DAO calls once the
        // Orders / Deliveries / Payments / Ratings modules are built.
        metrics.totalProducts = 0;
        metrics.pendingOrders = 0;
        metrics.completedOrders = 0;
        metrics.todaysDeliveries = 0;
        metrics.paymentsReceivedDisplay = "₹0.00";
        metrics.supplierRatingDisplay = "N/A";

        return metrics;
    }

    // ================================================================
    // RECENT ACTIVITY + NOTIFICATIONS
    // ================================================================

    /**
     * Builds the side-by-side "Recent Activity" and "Notifications"
     * section using GridLayout(1, 2) so both panels split the available
     * width evenly and resize responsively.
     *
     * @return the fully configured section JPanel
     */
    private JPanel buildRecentActivityAndNotificationsSection() {
        JPanel section = new JPanel(new GridLayout(1, 2, 20, 0));
        section.setOpaque(false);
        section.setPreferredSize(new Dimension(0, 260));

        section.add(buildRecentActivityPanel());
        section.add(buildNotificationsPanel());

        return section;
    }

    /**
     * Builds the "Recent Activity" card showing the latest orders,
     * deliveries, and payments. Currently populated via
     * {@link #loadRecentActivity()}, which returns an empty/placeholder
     * list until Order/Delivery/Payment DAOs exist.
     *
     * @return the fully configured Recent Activity JPanel
     */
    private JPanel buildRecentActivityPanel() {
        RoundedShadowPanel panel = new RoundedShadowPanel(16);
        panel.setLayout(new BorderLayout());
        panel.setBackground(COLOR_CARD_BG);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("Recent Activity");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        String[] activityItems = loadRecentActivity();

        if (activityItems.length == 0) {
            listPanel.add(buildEmptyStateLabel("No recent activity yet."));
        } else {
            for (String item : activityItems) {
                listPanel.add(buildActivityRow(item));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds a single row inside the Recent Activity list.
     *
     * @param text the activity description to display
     * @return a fully configured row JPanel
     */
    private JPanel buildActivityRow(String text) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel bullet = new JLabel("•");
        bullet.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bullet.setForeground(COLOR_PRIMARY);
        bullet.setBorder(new EmptyBorder(0, 0, 0, 10));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(FONT_CARD_LABEL);
        textLabel.setForeground(COLOR_TEXT_DARK);

        row.add(bullet, BorderLayout.WEST);
        row.add(textLabel, BorderLayout.CENTER);
        return row;
    }

    /**
     * FUTURE-READY: Loads the list of recent activity strings (latest
     * orders, deliveries, and payments combined and sorted by date).
     * Currently returns an empty array as a placeholder. A future step
     * can replace this with a real query such as:
     *   activityDAO.getRecentActivity(supplierId, limit)
     *
     * @return an array of human-readable activity descriptions
     */
    private String[] loadRecentActivity() {
        // TODO: Replace with real data once Orders/Deliveries/Payments
        // modules and their DAOs are implemented.
        return new String[0];
    }

    /**
     * Builds the "Notifications" card showing new orders, pending
     * deliveries, and payment alerts. Currently populated via
     * {@link #loadNotifications()}, which returns an empty/placeholder
     * list until the relevant modules exist.
     *
     * @return the fully configured Notifications JPanel
     */
    private JPanel buildNotificationsPanel() {
        RoundedShadowPanel panel = new RoundedShadowPanel(16);
        panel.setLayout(new BorderLayout());
        panel.setBackground(COLOR_CARD_BG);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("Notifications");
        title.setFont(FONT_SECTION_TITLE);
        title.setForeground(COLOR_TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        String[] notifications = loadNotifications();

        if (notifications.length == 0) {
            listPanel.add(buildEmptyStateLabel("No new notifications."));
        } else {
            for (String note : notifications) {
                listPanel.add(buildNotificationRow(note));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds a single row inside the Notifications list, using an
     * amber accent dot to distinguish it visually from activity rows.
     *
     * @param text the notification text to display
     * @return a fully configured row JPanel
     */
    private JPanel buildNotificationRow(String text) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dot.setForeground(COLOR_ACCENT);
        dot.setBorder(new EmptyBorder(0, 0, 0, 12));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(FONT_CARD_LABEL);
        textLabel.setForeground(COLOR_TEXT_DARK);

        row.add(dot, BorderLayout.WEST);
        row.add(textLabel, BorderLayout.CENTER);
        return row;
    }

    /**
     * FUTURE-READY: Loads the list of current notification strings
     * (new orders, pending deliveries, payment alerts). Currently
     * returns an empty array as a placeholder. A future step can
     * replace this with a real query such as:
     *   notificationDAO.getActiveNotifications(supplierId)
     *
     * @return an array of human-readable notification messages
     */
    private String[] loadNotifications() {
        // TODO: Replace with real data once Orders/Deliveries/Payments
        // modules and their DAOs are implemented.
        return new String[0];
    }

    /**
     * Builds a muted placeholder label shown inside Recent Activity /
     * Notifications panels when there is no data yet.
     *
     * @param message the placeholder message to display
     * @return a fully configured JLabel
     */
    private JLabel buildEmptyStateLabel(String message) {
        JLabel label = new JLabel(message);
        label.setFont(FONT_CARD_LABEL);
        label.setForeground(COLOR_TEXT_MUTED);
        label.setBorder(new EmptyBorder(10, 0, 0, 0));
        return label;
    }

    // ================================================================
    // FOOTER
    // ================================================================

    /**
     * Builds the footer bar shown at the bottom of the Dashboard card:
     * version number, copyright, and company name.
     *
     * @return the fully configured footer JPanel
     */
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 4, 4, 4));

        JLabel versionLabel = new JLabel("Grocery ERP System  v1.0.0");
        versionLabel.setFont(FONT_FOOTER);
        versionLabel.setForeground(COLOR_TEXT_MUTED);

        JLabel copyrightLabel = new JLabel("© 2026 Grocery ERP System — Supplier Portal. All rights reserved.");
        copyrightLabel.setFont(FONT_FOOTER);
        copyrightLabel.setForeground(COLOR_TEXT_MUTED);
        copyrightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        footer.add(versionLabel, BorderLayout.WEST);
        footer.add(copyrightLabel, BorderLayout.EAST);

        return footer;
    }

    // ================================================================
    // ROUNDED UI HELPER COMPONENTS
    // ================================================================

    /**
     * A JPanel subclass that paints itself with rounded corners and a
     * solid fill color, used for badges and small accent shapes.
     */
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

    /**
     * A JPanel subclass that paints itself with rounded corners and a
     * soft drop shadow, used for the dashboard's metric/activity/
     * notification cards. Supports a "hovered" state that deepens the
     * shadow and nudges the card upward slightly, producing a simple
     * hover-lift animation without any external animation library.
     */
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

            // Soft shadow, offset downward — deeper and larger on hover
            g2.setColor(new Color(0, 0, 0, shadowAlpha));
            g2.fill(new RoundRectangle2D.Float(
                    shadowOffset, shadowOffset,
                    getWidth() - shadowOffset, getHeight() - shadowOffset,
                    cornerRadius, cornerRadius));

            // Card surface itself, lifted slightly on hover
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

    /**
     * Creates a professionally styled rounded button with hover
     * feedback, used throughout the navbar and future action areas.
     *
     * @param text        the button label
     * @param baseColor   the button's normal background color
     * @param hoverColor  the button's background color on mouse hover
     * @param width       preferred button width
     * @param height      preferred button height
     * @return a fully configured rounded JButton
     */
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

    /**
     * Handles the Logout button click: confirms with the user, then
     * closes this dashboard and returns to LoginSelectionFrame.
     */
    private JPanel buildProductsPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JButton btn = new JButton("Open Product Management");

        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));

        btn.addActionListener(e -> {
        	new ProductManagementFrame(loggedInSupplier);
        });

        panel.add(btn, BorderLayout.CENTER);

        return panel;
    }
    private JPanel buildOrdersPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY_LIGHT);

        JButton btn = new JButton("Open Order Management");

        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));

        btn.addActionListener(e -> {
            new OrderManagementFrame_old(loggedInSupplier);
        });

        panel.add(btn, BorderLayout.CENTER);

        return panel;
    }
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