package com.grocery.gui;

import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;
import com.grocery.model.Admin;

import javax.swing.*;
import java.awt.*;

/**
 * UpdateAdminFrame
 * -----------------
 * Screen for searching and updating a single admin, opened from
 * AdminManagementFrame.
 *
 * Search button looks up an admin by username and populates the form.
 * Update button is connected to MySQL via AdminDAO — it validates
 * the form, builds an Admin object, and calls adminDAO.updateAdmin(admin).
 */
public class UpdateAdminFrame extends JFrame {

    // Same theme palette as AddAdminFrame / ViewAdminFrame / SearchAdminFrame
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color RESULT_PANEL_BG = Color.WHITE;

    // DAO instance used to look up and update an admin
    private final AdminDAO adminDAO = new AdminDAOImpl();

    private JTextField searchUsernameField;
    private JButton searchBtn;

    private JLabel adminIdValue;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;

    private JButton updateBtn;
    private JButton resetBtn;
    private JButton backBtn;

    public UpdateAdminFrame() {
        initFrame();
        initTopPanel();
        initFormPanel();
        initButtonPanel();

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {

        setTitle("Update Admin ");

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

        JLabel titleLabel = new JLabel("✏  Update Admin");
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

        searchUsernameField = new JTextField();
        searchUsernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchPanel.add(searchUsernameField, gbc);

        searchBtn = createStyledButton("🔍  Search", BUTTON_GREEN);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 5, 5, 5);
        searchPanel.add(searchBtn, gbc);

        searchBtn.addActionListener(e -> handleSearch());

        return searchPanel;
    }

    /**
     * Panel displaying the admin information fields to be updated.
     * Admin ID is a read-only label; Full Name, Username, and Email
     * are editable text fields. All start empty / "-" until a search
     * is performed.
     */
    private void initFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(RESULT_PANEL_BG);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 14);

        // Admin ID (read-only display)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Admin ID:", labelFont), gbc);
        adminIdValue = createLabel("-", valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(adminIdValue, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Full Name:", labelFont), gbc);
        fullNameField = new JTextField();
        fullNameField.setFont(valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(fullNameField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Username:", labelFont), gbc);
        usernameField = new JTextField();
        usernameField.setFont(valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Email:", labelFont), gbc);
        emailField = new JTextField();
        emailField.setFont(valueFont);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(emailField, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_GREEN);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        wrapper.add(formPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Update, Reset, and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        updateBtn = createStyledButton("Update", BUTTON_GREEN);
        resetBtn = createStyledButton("Reset", RESET_GRAY);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        updateBtn.addActionListener(e -> handleUpdate());

        resetBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });

        buttonPanel.add(updateBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the Search button click:
     * 1. Validates that a username was entered.
     * 2. Looks up the admin via AdminDAO.getAdminByUsername().
     * 3. Populates the form fields on success, or shows
     *    "Admin Not Found." and clears fields on failure.
     */
    private void handleSearch() {
        String username = searchUsernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin != null) {
            adminIdValue.setText(String.valueOf(admin.getAdminId()));
            fullNameField.setText(admin.getFullName());
            usernameField.setText(admin.getUsername());
            emailField.setText(admin.getEmail());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Admin Not Found.",
                    "Not Found", JOptionPane.WARNING_MESSAGE);

            adminIdValue.setText("-");
            fullNameField.setText("");
            usernameField.setText("");
            emailField.setText("");
        }
    }

    /**
     * Handles the Update button click:
     * 1. Validates that Full Name, Username, and Email are not empty.
     * 2. Builds an Admin object with the current Admin ID and the
     *    field values.
     * 3. Calls adminDAO.updateAdmin(admin).
     * 4. Shows a success or failure message based on the result.
     */
    private void handleUpdate() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Admin admin = new Admin();
        admin.setAdminId(Integer.parseInt(adminIdValue.getText()));
        admin.setFullName(fullName);
        admin.setUsername(username);
        admin.setEmail(email);

        boolean success = adminDAO.updateAdmin(admin);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Admin updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update admin.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all text fields and resets the Admin ID label back to "-".
     */
    private void resetForm() {
        searchUsernameField.setText("");
        adminIdValue.setText("-");
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        searchUsernameField.requestFocus();
    }

    /**
     * Helper to create a styled JLabel.
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