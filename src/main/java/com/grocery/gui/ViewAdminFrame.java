package com.grocery.gui;

import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;
import com.grocery.model.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewAdminFrame
 * --------------
 * Screen for viewing all admin accounts in a tabular format, opened from
 * AdminManagementFrame.
 *
 * The JTable is now populated with live data from the database via
 * AdminDAO.getAllAdmins().
 */
public class ViewAdminFrame extends JFrame {

    // Same theme palette as the rest of the application
    private static final Color DARK_GREEN = new Color(27, 94, 32);
    private static final Color LIGHT_GREEN = new Color(232, 245, 233);
    private static final Color BUTTON_GREEN = new Color(56, 142, 60);
    private static final Color BACK_GRAY = new Color(117, 117, 117);
    private static final Color TABLE_HEADER_GREEN = new Color(46, 125, 50);

    // DAO instance used to fetch all admin records from MySQL
    private final AdminDAO adminDAO = new AdminDAOImpl();

    private JTable adminTable;
    private DefaultTableModel tableModel;

    private JButton refreshBtn;
    private JButton backBtn;

    public ViewAdminFrame() {
        initFrame();
        initHeader();
        initTable();
        initButtonPanel();

        loadAdminData(); // populate the table as soon as the screen opens

        setVisible(true);
    }

    /**
     * Configures core JFrame properties and centers the window.
     */
    private void initFrame() {

        setTitle("View Existing Admin ");

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
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel titleLabel = new JLabel("📋  View Admins");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Center panel containing the JTable (inside a JScrollPane) that
     * displays all admin records.
     */
    private void initTable() {
        String[] columnNames = {"Admin ID", "Full Name", "Username", "Email", "Created At"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        adminTable = new JTable(tableModel);
        adminTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        adminTable.setRowHeight(26);
        adminTable.setSelectionBackground(new Color(200, 230, 201));
        adminTable.setSelectionForeground(Color.BLACK);
        adminTable.setGridColor(new Color(224, 224, 224));
        adminTable.setShowGrid(true);

        adminTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        adminTable.getTableHeader().setBackground(TABLE_HEADER_GREEN);
        adminTable.getTableHeader().setForeground(Color.WHITE);
        adminTable.getTableHeader().setPreferredSize(new Dimension(0, 32));

        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(LIGHT_GREEN);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        add(tableWrapper, BorderLayout.CENTER);
    }

    /**
     * Bottom panel with Refresh and Back buttons.
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_GREEN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        refreshBtn = createStyledButton("🔄  Refresh", BUTTON_GREEN);
        backBtn = createStyledButton("⬅  Back", BACK_GRAY);

        refreshBtn.addActionListener(e -> {
            loadAdminData();
            JOptionPane.showMessageDialog(this,
                    "Data Refreshed Successfully.",
                    "Refreshed", JOptionPane.INFORMATION_MESSAGE);
        });

        backBtn.addActionListener(e -> {
            new AdminManagementFrame();
            dispose();
        });

        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Fetches all admin records from the database via AdminDAO and
     * populates the JTable. Clears any existing rows first so repeated
     * calls (e.g. via Refresh) don't duplicate data.
     */
    private void loadAdminData() {
        // Remove all existing rows from the table model
        tableModel.setRowCount(0);

        List<Admin> adminList = adminDAO.getAllAdmins();

        if (adminList == null || adminList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No Admin Records Found.",
                    "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Admin admin : adminList) {
            Object[] row = {
                    admin.getAdminId(),
                    admin.getFullName(),
                    admin.getUsername(),
                    admin.getEmail(),
                    admin.getCreatedAt()
            };
            tableModel.addRow(row);
        }
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
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }
}