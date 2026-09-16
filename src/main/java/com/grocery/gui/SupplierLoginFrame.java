package com.grocery.gui;

import com.grocery.dao.SupplierDAO;
import com.grocery.dao.impl.SupplierDAOImpl;
import com.grocery.model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 * SupplierLoginFrame.java
 *
 * Professional Java Swing login screen for the Supplier role.
 * Follows the same green-themed, enterprise look used across the rest
 * of the application (e.g. AdminLoginFrame, LoginSelectionFrame).
 *
 * On successful authentication, opens SupplierDashboard and closes
 * this login window. Uses the existing SupplierDAO / SupplierDAOImpl /
 * Supplier classes exactly as defined in the project.
 */
public class SupplierLoginFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with the rest of the application)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_CARD_BG      = Color.WHITE;
    private static final Color COLOR_CARD_BORDER  = new Color(200, 230, 201);

    private static final Color COLOR_LOGIN_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_LOGIN_HOVER = new Color(27, 94, 32);    // Darker green on hover
    private static final Color COLOR_BACK_BG      = new Color(97, 97, 97);   // Neutral gray
    private static final Color COLOR_BACK_HOVER   = new Color(66, 66, 66);
    private static final Color COLOR_BUTTON_TEXT  = Color.WHITE;

    private static final Font FONT_LOGO    = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_FIELD   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 16);

    // DAO instance used to authenticate the supplier
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    private JButton loginButton;
    private JButton backButton;

    /**
     * Constructs and displays the Supplier Login screen.
     */
    public SupplierLoginFrame() {
        initializeFrame();
        buildUI();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, close behaviour,
     * and centers the window on screen.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - Supplier Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);   // Full Screen
        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_BODY_BG);
    }
    /**
     * Builds the complete UI: header banner, login card, and footer.
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the top header banner showing the company title and a
     * short subtitle identifying this as the Supplier login screen.
     *
     * @return the fully configured header JPanel
     */
    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel logoLabel = new JLabel("🛒  Grocery Management System", SwingConstants.CENTER);
        logoLabel.setFont(FONT_LOGO);
        logoLabel.setForeground(COLOR_HEADER_TEXT);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("🚚  Supplier Login Portal", SwingConstants.CENTER);
        subtitleLabel.setFont(FONT_SUBTEXT);
        subtitleLabel.setForeground(new Color(220, 237, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        headerPanel.add(logoLabel);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    /**
     * Builds the central content area containing the login form,
     * inside a centered white card.
     *
     * @return the fully configured center JPanel
     */
    private JPanel buildCenterPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(COLOR_BODY_BG);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(30, 35, 25, 35)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label + field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        cardPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(FONT_FIELD);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cardPanel.add(usernameField, gbc);

        // Password label + field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(FONT_LABEL);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        cardPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(FONT_FIELD);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cardPanel.add(passwordField, gbc);

        // Show Password checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(COLOR_CARD_BG);
        showPasswordCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 2;
        cardPanel.add(showPasswordCheckBox, gbc);

        // Toggles password visibility by changing the echo character
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // 0 = show plain text
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });

        // Button row: Login, Back
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(COLOR_CARD_BG);

        loginButton = createStyledButton("Login", COLOR_LOGIN_BG, COLOR_LOGIN_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleLogin();
                    }
                });

        backButton = createStyledButton("Back", COLOR_BACK_BG, COLOR_BACK_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new LoginSelectionFrame();
                    }
                });

        buttonRow.add(loginButton);
        buttonRow.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 5, 5);
        cardPanel.add(buttonRow, gbc);

        // Pressing Enter in either field triggers login, for convenience
        EnterKeyLoginListener enterKeyListener = new EnterKeyLoginListener();
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

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
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel footerLabel = new JLabel("© 2026 Grocery Management System | Supplier Portal");
        footerLabel.setForeground(COLOR_HEADER_TEXT);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    // ---------------------------------------------------------
    // Helper method: creates a consistently styled button
    // ---------------------------------------------------------

    /**
     * Creates a professionally styled button, complete with hover
     * feedback and a click handler.
     *
     * @param text           the button label
     * @param baseColor      the button's normal background color
     * @param hoverColor     the button's background color on mouse hover
     * @param actionListener the action to run when the button is clicked
     * @return a fully configured JButton
     */
    private JButton createStyledButton(String text, Color baseColor, Color hoverColor,
                                        ActionListener actionListener) {

        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(COLOR_BUTTON_TEXT);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(140, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

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
    // Business logic: login handling
    // ---------------------------------------------------------

    /**
     * Handles the Login button click (and Enter key press):
     * 1. Validates that both fields are filled.
     * 2. Authenticates via SupplierDAO.login(username, password).
     * 3. On success, opens SupplierDashboard and closes this window.
     * 4. On failure, shows an "Invalid Username or Password" message.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // ---- Basic field validation ----
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Supplier supplier = supplierDAO.login(username, password);

            if (supplier != null) {
                JOptionPane.showMessageDialog(this,
                        "Login successful! Welcome, " + supplier.getSupplierName() + ".",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                new SupplierDashboard(supplier); // open the supplier dashboard
                dispose();                       // close this login window
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText(""); // clear password field on failure
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error during login: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Key listener that triggers login when the Enter key is pressed
     * inside the username or password field, for a smoother login
     * experience (no need to click the Login button).
     */
    private class EnterKeyLoginListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                handleLogin();
            }
        }
    }
}