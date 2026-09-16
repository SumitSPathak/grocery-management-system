package com.grocery.gui;

import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;
import com.grocery.model.Admin;

import javax.swing.*;
import java.awt.*;

/**
 * SearchAdminFrame
 * -----------------
 * Screen for searching a single admin by username, opened from
 * AdminManagementFrame.
 *
 * Layout fix: headerPanel and searchPanel were both being added directly
 * to BorderLayout.NORTH, which caused one to overwrite the other. They are
 * now combined into a single topPanel (BorderLayout) — headerPanel in
 * NORTH, searchPanel in CENTER — and topPanel is added once to the frame's
 * BorderLayout.NORTH. No search/reset/DAO logic was touched.
 */
public class SearchAdminFrame extends JFrame {

    // Same theme palette as AddAdminFrame / ViewAdminFrame
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color RESULT_PANEL_BG = Color.WHITE;

    // DAO instance used to look up an admin by username
    private final AdminDAO adminDAO = new AdminDAOImpl();

    private JTextField usernameField;
    private JButton searchBtn;

    private JLabel adminIdValue;
    private JLabel fullNameValue;
    private JLabel usernameValue;
    private JLabel emailValue;
    private JLabel createdAtValue;

    private JButton resetBtn;
    private JButton backBtn;

    public SearchAdminFrame() {
        initFrame();
        initTopPanel();      // combines header + search panel, adds once to NORTH
        initResultPanel();
        initButtonPanel();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {

        setTitle("Search Admin");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(true);

        setLayout(new BorderLayout());

        getContentPane().setBackground(LIGHT_GREEN);
    }
    /**
     * Builds the header panel (title bar) and the search panel
     * (username field + Search button), then combines them into a single
     * topPanel so only one component occupies BorderLayout.NORTH on the
     * main frame.
     */
    private void initTopPanel() {
        JPanel headerPanel = buildHeaderPanel();
        JPanel searchPanel = buildSearchPanel();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Top header showing the screen title.
     */
    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("🔍  Search Admin");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        return headerPanel;
    }

    /**
     * Panel with the username field and Search button.
     */
    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(LIGHT_GREEN);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        searchPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchPanel.add(usernameField, gbc);

        searchBtn = createStyledButton("🔍  Search", BUTTON_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 5, 5, 5);
        searchPanel.add(searchBtn, gbc);

        searchBtn.addActionListener(e -> handleSearch());

        return searchPanel;
    }

    /**
     * Panel displaying the search result fields. All values default to "-"
     * until a search is performed.
     */
    private void initResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setBackground(RESULT_PANEL_BG);
        resultPanel.setLayout(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 14);

        // Admin ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        resultPanel.add(createLabel("Admin ID:", labelFont), gbc);
        adminIdValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        resultPanel.add(adminIdValue, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        resultPanel.add(createLabel("Full Name:", labelFont), gbc);
        fullNameValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        resultPanel.add(fullNameValue, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        resultPanel.add(createLabel("Username:", labelFont), gbc);
        usernameValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        resultPanel.add(usernameValue, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        resultPanel.add(createLabel("Email:", labelFont), gbc);
        emailValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        resultPanel.add(emailValue, gbc);

        // Created At
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.4;
        resultPanel.add(createLabel("Created At:", labelFont), gbc);
        createdAtValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        resultPanel.add(createdAtValue, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_GREEN);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        wrapper.add(resultPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Reset and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        resetBtn = createStyledButton("Reset", RESET_GRAY);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        resetBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });

        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the Search button click:
     * 1. Validates that a username was entered.
     * 2. Looks up the admin via AdminDAO.getAdminByUsername().
     * 3. Populates the result labels on success, or shows
     *    "Admin Not Found." and resets labels on failure.
     */
    private void handleSearch() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin != null) {
            adminIdValue.setText(String.valueOf(admin.getAdminId()));
            fullNameValue.setText(admin.getFullName());
            usernameValue.setText(admin.getUsername());
            emailValue.setText(admin.getEmail());
            createdAtValue.setText(String.valueOf(admin.getCreatedAt()));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Admin Not Found.",
                    "Not Found", JOptionPane.WARNING_MESSAGE);

            adminIdValue.setText("-");
            fullNameValue.setText("-");
            usernameValue.setText("-");
            emailValue.setText("-");
            createdAtValue.setText("-");
        }
    }

    /**
     * Clears the username field and resets all result labels back to "-".
     */
    private void resetForm() {
        usernameField.setText("");
        adminIdValue.setText("-");
        fullNameValue.setText("-");
        usernameValue.setText("-");
        emailValue.setText("-");
        createdAtValue.setText("-");
        usernameField.requestFocus();
    }

    /**
     * Helper to create a styled JLabel for the result panel.
     */
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
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
        button.setPreferredSize(new Dimension(140, 38));
        return button;
    }
}