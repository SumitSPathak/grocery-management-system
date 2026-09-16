package com.grocery.gui;

import javax.swing.*;
import java.awt.*;

/**
 * AdminManagementFrame
 * ---------------------
 * Admin Management screen, opened from MainDashboard via the
 * "Admin Management" button. Provides navigation to Add/View/Search/
 * Update/Delete Admin operations.
 *
 * This step builds ONLY the UI shell — no database operations are wired
 * yet. Each button's ActionListener will be connected to its own screen
 * in future steps.
 */
public class AdminManagementFrame extends JFrame {

    // Same theme palette as the rest of the application
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color BACK_GRAY = new Color(117, 117, 117);

    private JButton addAdminBtn;
    private JButton viewAdminBtn;
    private JButton searchAdminBtn;
    private JButton updateAdminBtn;
    private JButton deleteAdminBtn;
    private JButton backBtn;

    public AdminManagementFrame() {
        initFrame();
        initHeader();
        initButtonPanel();
        initFooter();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {

        setTitle("Admin Mangament");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(true);

        setLayout(new BorderLayout());

        getContentPane().setBackground(LIGHT_GREEN);
    }
    /**
     * Top header showing the screen title.
     */
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("👑  Admin Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center panel with the six management buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        addAdminBtn = createStyledButton("➕  Add Admin", BUTTON_GREEN);
        viewAdminBtn = createStyledButton("📋  View Admin", BUTTON_GREEN);
        searchAdminBtn = createStyledButton("🔍  Search Admin", BUTTON_GREEN);
        updateAdminBtn = createStyledButton("✏️  Update Admin", BUTTON_GREEN);
        deleteAdminBtn = createStyledButton("🗑️  Delete Admin", BUTTON_GREEN);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        // Placeholder actions only — real screens/DB operations wired in future steps
        addAdminBtn.addActionListener(e -> {
            new AddAdminFrame();
            dispose();
        });
         viewAdminBtn.addActionListener(e -> {
            new ViewAdminFrame();
            dispose();
        });
         searchAdminBtn.addActionListener(e -> {
        	    new SearchAdminFrame();
        	    dispose();
        	});
         updateAdminBtn.addActionListener(e -> {
        	    new UpdateAdminFrame();
        	    dispose();
        	});
          deleteAdminBtn.addActionListener(e -> {
        	    new DeleteAdminFrame();
        	    dispose();
        	});

        backBtn.addActionListener(e -> {
            new MainDashboard();
            dispose();
        });

        buttonPanel.add(addAdminBtn);
        buttonPanel.add(viewAdminBtn);
        buttonPanel.add(searchAdminBtn);
        buttonPanel.add(updateAdminBtn);
        buttonPanel.add(deleteAdminBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Bottom footer strip.
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

    private void showComingSoon(String feature) {
        JOptionPane.showMessageDialog(this,
                feature + " screen will be created in a future step.",
                "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Helper to create a consistently styled button with a custom background color.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}