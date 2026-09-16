package com.grocery.gui;

import com.grocery.dao.SupplierDAO;
import com.grocery.dao.impl.SupplierDAOImpl;
import com.grocery.model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * ViewSuppliersFrame.java
 *
 * Professional Java Swing screen for viewing all supplier records in a
 * tabular format. Follows the same green-themed, full-screen layout
 * used across the rest of the application (e.g. SupplierManagementFrame,
 * AddSupplierFrame).
 *
 * Uses the existing SupplierDAO / SupplierDAOImpl / Supplier classes
 * exactly as they are defined in the project — no new methods or
 * fields are assumed.
 */
public class ViewSuppliersFrame extends JFrame {

    // ---------------------------------------------------------
    // Theme colors (kept consistent with SupplierManagementFrame / AddSupplierFrame)
    // ---------------------------------------------------------
    private static final Color COLOR_HEADER_BG   = new Color(27, 94, 32);    // Dark green
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_BODY_BG      = new Color(232, 245, 233); // Light green background
    private static final Color COLOR_CARD_BG      = Color.WHITE;
    private static final Color COLOR_CARD_BORDER  = new Color(200, 230, 201);

    private static final Color COLOR_REFRESH_BG    = new Color(46, 125, 50);   // Medium green
    private static final Color COLOR_REFRESH_HOVER = new Color(27, 94, 32);    // Darker green on hover
    private static final Color COLOR_BACK_BG        = new Color(198, 40, 40);  // Red accent
    private static final Color COLOR_BACK_HOVER     = new Color(140, 20, 20);
    private static final Color COLOR_BUTTON_TEXT    = Color.WHITE;
    private static final Color COLOR_TABLE_HEADER    = new Color(46, 125, 50);

    private static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 16);

    // DAO instance used to fetch all supplier records
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    private JButton refreshButton;
    private JButton backButton;

    /**
     * Constructs and displays the View Suppliers screen.
     */
    public ViewSuppliersFrame() {
        initializeFrame();
        buildUI();
        loadSupplierData();
        setVisible(true);
    }

    /**
     * Configures the base JFrame settings: title, size, full-screen
     * behaviour, close operation, and default look.
     */
    private void initializeFrame() {
        setTitle("Grocery Management System - View Suppliers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BODY_BG);
    }

    /**
     * Builds the complete UI: header banner, table card, and footer.
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the top header banner showing the screen title.
     *
     * @return the fully configured header JPanel
     */
    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_HEADER_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel("View Suppliers", SwingConstants.CENTER);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_HEADER_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Browse all supplier records currently stored in the system",
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
     * Builds the central content area containing the table card.
     *
     * @return the fully configured center JPanel
     */
    private JPanel buildCenterPanel() {
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(COLOR_BODY_BG);
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(30, 60, 20, 60));

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));

        JLabel cardHeading = new JLabel("All Suppliers");
        cardHeading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardHeading.setForeground(COLOR_HEADER_BG);
        cardHeading.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        cardPanel.add(cardHeading, BorderLayout.NORTH);

        String[] columnNames = {
                "Supplier ID", "Supplier Name", "Company Name",
                "Phone", "Email", "Address", "GST Number", "Created At"
        };

        // Empty, non-editable table model — populated by loadSupplierData()
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        supplierTable = new JTable(tableModel);
        supplierTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        supplierTable.setRowHeight(28);
        supplierTable.setSelectionBackground(new Color(200, 230, 201));
        supplierTable.setSelectionForeground(Color.BLACK);
        supplierTable.setGridColor(new Color(224, 224, 224));
        supplierTable.setShowGrid(true);

        supplierTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        supplierTable.getTableHeader().setBackground(COLOR_TABLE_HEADER);
        supplierTable.getTableHeader().setForeground(Color.WHITE);
        supplierTable.getTableHeader().setPreferredSize(new Dimension(0, 34));

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_CARD_BORDER));

        cardPanel.add(scrollPane, BorderLayout.CENTER);

        // Button row: Refresh, Back
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(COLOR_CARD_BG);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        refreshButton = createStyledButton("Refresh", COLOR_REFRESH_BG, COLOR_REFRESH_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadSupplierData();
                    }
                });

        backButton = createStyledButton("Back", COLOR_BACK_BG, COLOR_BACK_HOVER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new SupplierManagementFrame();
                    }
                });

        buttonRow.add(refreshButton);
        buttonRow.add(backButton);

        cardPanel.add(buttonRow, BorderLayout.SOUTH);

        outerWrapper.add(cardPanel, BorderLayout.CENTER);
        return outerWrapper;
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
        button.setPreferredSize(new Dimension(140, 42));
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
    // Business logic: loading data from the database
    // ---------------------------------------------------------

    /**
     * Fetches all supplier records from the database via SupplierDAO
     * and populates the JTable. Clears any existing rows first so
     * repeated calls (e.g. via Refresh) don't duplicate data.
     */
    private void loadSupplierData() {
        tableModel.setRowCount(0);

        try {
            List<Supplier> supplierList = supplierDAO.getAllSuppliers();

            if (supplierList == null || supplierList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No supplier records found.",
                        "Empty", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Supplier supplier : supplierList) {
                Object[] row = {
                        supplier.getSupplierId(),
                        supplier.getSupplierName(),
                        supplier.getCompanyName(),
                        supplier.getPhone(),
                        supplier.getEmail(),
                        supplier.getAddress(),
                        supplier.getGstNumber(),
                        supplier.getCreatedAt()
                };
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error while loading suppliers: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}