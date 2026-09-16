package com.grocery.gui;

import javax.swing.*;
import java.awt.*;
import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;
import com.grocery.model.Admin;

public class DeleteAdminFrame extends JFrame {

    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color RESET_GRAY = new Color(97, 97, 97);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color RESULT_PANEL_BG = Color.WHITE;

    private JTextField searchField;

    private JLabel lblAdminIdValue;
    private JLabel lblFullNameValue;
    private JLabel lblUsernameValue;
    private JLabel lblEmailValue;
    private AdminDAO adminDAO = new AdminDAOImpl();
    public DeleteAdminFrame() {
        initFrame();
        initTopPanel();
        initResultPanel();
        initButtonPanel();
        setVisible(true);
    }

    private void initFrame() {

        setTitle("Delete Existing Admin ");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setResizable(true);

        setLayout(new BorderLayout());

        getContentPane().setBackground(LIGHT_GREEN);
    }
    private void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.add(buildHeaderPanel(), BorderLayout.NORTH);
        topPanel.add(buildSearchPanel(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(500, 70));

        JLabel headerLabel = new JLabel("🗑 Delete Admin");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);

        headerPanel.add(headerLabel);
        return headerPanel;
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(LIGHT_GREEN);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = createLabel("Username:");
        searchPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        searchField = new JTextField(15);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchPanel.add(searchField, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;

        JButton searchButton = createStyledButton("Search", BUTTON_GREEN);

        searchButton.addActionListener(e -> handleSearch());

        searchPanel.add(searchButton, gbc);

        return searchPanel;

    }

    private void initResultPanel() {
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBackground(RESULT_PANEL_BG);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        resultPanel.add(createLabel("Admin ID:"), gbc);

        gbc.gridx = 1;
        lblAdminIdValue = new JLabel("-");
        lblAdminIdValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultPanel.add(lblAdminIdValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        resultPanel.add(createLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        lblFullNameValue = new JLabel("-");
        lblFullNameValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultPanel.add(lblFullNameValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        resultPanel.add(createLabel("Username:"), gbc);

        gbc.gridx = 1;
        lblUsernameValue = new JLabel("-");
        lblUsernameValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultPanel.add(lblUsernameValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        resultPanel.add(createLabel("Email:"), gbc);

        gbc.gridx = 1;
        lblEmailValue = new JLabel("-");
        lblEmailValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultPanel.add(lblEmailValue, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_GREEN);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        wrapper.add(resultPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        JButton deleteButton = createStyledButton("Delete", BUTTON_GREEN);
        deleteButton.addActionListener(e -> {

            if (lblAdminIdValue.getText().equals("-")) {

                JOptionPane.showMessageDialog(this,
                        "Please search an admin first.",
                        "Delete Admin",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this admin?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {

                int adminId = Integer.parseInt(lblAdminIdValue.getText());

                if (adminDAO.deleteAdmin(adminId)) {

                    JOptionPane.showMessageDialog(this,
                            "Admin deleted successfully.");

                    resetForm();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Unable to delete admin.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        JButton resetButton = createStyledButton("Reset", RESET_GRAY);
        resetButton.addActionListener(e -> resetForm());

        JButton backButton = createStyledButton("Back", BACK_GRAY);
        backButton.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void handleSearch() {

        String username = searchField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin == null) {

            JOptionPane.showMessageDialog(this,
                    "Admin not found.",
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE);

            resetForm();
            return;
        }

        lblAdminIdValue.setText(String.valueOf(admin.getAdminId()));
        lblFullNameValue.setText(admin.getFullName());
        lblUsernameValue.setText(admin.getUsername());
        lblEmailValue.setText(admin.getEmail());
    }
    private void resetForm() {
        searchField.setText("");
        lblAdminIdValue.setText("-");
        lblFullNameValue.setText("-");
        lblUsernameValue.setText("-");
        lblEmailValue.setText("-");
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
}