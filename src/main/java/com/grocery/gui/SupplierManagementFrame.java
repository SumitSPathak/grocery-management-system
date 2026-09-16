package com.grocery.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SupplierManagementFrame.java
 *
 * Professional dashboard screen for the Supplier module.
 * Provides quick navigation to every Supplier-related operation:
 *      Add Supplier, View Suppliers, Search Supplier,
 *      Update Supplier, Delete Supplier, and Back to Dashboard.
 *
 * Follows the same green-themed, full-screen layout used across
 * the rest of the application (e.g. AdminManagementFrame,
 * ProductManagementFrame) so the module feels like a natural
 * part of the same system.
 *
 * NOTE: This file assumes the following classes already exist in the
 * "com.grocery.gui" package (they are generated separately, one by one,
 * per the project plan): AddSupplierFrame, ViewSuppliersFrame,
 * SearchSupplierFrame, UpdateSupplierFrame, DeleteSupplierFrame,
 * and MainDashboard.
 */
public class SupplierManagementFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with the rest of the system)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_BUTTON_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_BUTTON_HOVER = new Color(27, 94, 32);    // Darker green on hover
    private static final Color COLOR_BUTTON_TEXT  = Color.WHITE;
    private static final Color COLOR_BACK_BUTTON  = new Color(198, 40, 40);   // Red accent for Back
    private static final Color COLOR_BACK_HOVER   = new Color(140, 20, 20);

    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);

    /**
     * Constructs and displays the Supplier Management dashboard.
     */
    public SupplierManagementFrame() {
        initializeFrame();
        buildUI();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, full-screen
     * behaviour, close operation, and default look.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - Supplier Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Full-screen (maximized) window, while still allowing the user
        // to resize/restore it, unlike a true undecorated full-screen mode.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BODY_BG);
    }

    /**
     * Builds the complete UI: header banner, button grid, and footer.
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the top header banner showing the module title.
     *
     * @return the fully configured header JPanel
     */
    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel("Supplier Management", SwingConstants.CENTER);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_HEADER_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Manage all supplier records: add, view, search, update, and delete",
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
     * Builds the central content area containing the grid of
     * navigation buttons, centered both horizontally and vertically.
     *
     * @return the fully configured center JPanel
     */
    private JPanel buildCenterPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(COLOR_BODY_BG);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 201), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)));

        cardPanel.setLayout(new GridLayout(3, 2, 25, 25));
        cardPanel.setPreferredSize(new Dimension(650, 340));

        // Each button below is created through the shared helper method
        // and wired to open its corresponding frame.
        cardPanel.add(createNavigationButton("Add Supplier", COLOR_BUTTON_BG, COLOR_BUTTON_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openAddSupplierFrame();
                    }
                }));

        cardPanel.add(createNavigationButton("View Suppliers", COLOR_BUTTON_BG, COLOR_BUTTON_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openViewSuppliersFrame();
                    }
                }));

        cardPanel.add(createNavigationButton("Search Supplier", COLOR_BUTTON_BG, COLOR_BUTTON_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openSearchSupplierFrame();
                    }
                }));

        cardPanel.add(createNavigationButton("Update Supplier", COLOR_BUTTON_BG, COLOR_BUTTON_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openUpdateSupplierFrame();
                    }
                }));

        cardPanel.add(createNavigationButton("Delete Supplier", COLOR_BUTTON_BG, COLOR_BUTTON_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openDeleteSupplierFrame();
                    }
                }));

        cardPanel.add(createNavigationButton("Back to Dashboard", COLOR_BACK_BUTTON, COLOR_BACK_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openMainDashboard();
                    }
                }));

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
    // Helper method: creates a consistently styled navigation button
    // ---------------------------------------------------------

    /**
     * Creates a professionally styled button used for dashboard
     * navigation, complete with hover feedback and a click handler.
     *
     * @param text          the button label
     * @param baseColor     the button's normal background color
     * @param hoverColor    the button's background color on mouse hover
     * @param actionListener the action to run when the button is clicked
     * @return a fully configured JButton
     */
    private JButton createNavigationButton(String text, Color baseColor, Color hoverColor,
                                            ActionListener actionListener) {

        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(COLOR_BUTTON_TEXT);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        // Hover effect: darken the button while the mouse is over it,
        // and restore the original color when the mouse leaves.
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
    // Helper methods: navigation to other frames
    // ---------------------------------------------------------

    /**
     * Opens the Add Supplier form and closes this dashboard.
     */
    private void openAddSupplierFrame() {
        dispose();
        new AddSupplierFrame();
    }

    /**
     * Opens the View Suppliers table screen and closes this dashboard.
     */
    private void openViewSuppliersFrame() {
        dispose();
        new ViewSuppliersFrame();
    }

    /**
     * Opens the Search Supplier screen and closes this dashboard.
     */
    private void openSearchSupplierFrame() {
        dispose();
        new SearchSupplierFrame();
    }

    /**
     * Opens the Update Supplier screen and closes this dashboard.
     */
    private void openUpdateSupplierFrame() {
        dispose();
        new UpdateSupplierFrame();
    }

    /**
     * Opens the Delete Supplier screen and closes this dashboard.
     */
    private void openDeleteSupplierFrame() {
        dispose();
        new DeleteSupplierFrame();
    }

    /**
     * Returns to the Main Dashboard and closes this screen.
     */
    private void openMainDashboard() {
        dispose();
        new MainDashboard();
    }
}