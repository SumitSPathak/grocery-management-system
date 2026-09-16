package com.grocery.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;
import com.grocery.model.Order;
import com.grocery.model.Supplier;



public class OrderManagementFrame_old extends JFrame {


    private static final Color DARK_GREEN = new Color(27,94,32);
    private static final Color LIGHT_GREEN = new Color(232,245,233);
    private static final Color BUTTON_GREEN = new Color(56,142,60);
    private static final Color DELETE_RED = new Color(198,40,40);
    private static final Color SIDEBAR_BG = new Color(21,71,24);


    private Supplier loggedInSupplier;


    private final OrderDAO orderDAO = new OrderDAOImpl();



    private JButton createOrderBtn;
    private JButton viewOrdersBtn;
    private JButton searchOrderBtn;
    private JButton updateStatusBtn;
    private JButton cancelOrderBtn;

    private JButton deleteOrderBtn;
    private JButton backBtn;

    private JComboBox<String> statusFilter;
    private JComboBox<String> paymentFilter;
    private JComboBox<String> deliveryFilter;

    private JButton applyFilterBtn;
    
    private JLabel totalOrdersValue;
    private JLabel pendingOrdersValue;
    private JLabel completedOrdersValue;
    private JLabel cancelledOrdersValue;



    private JTable orderTable;

    private DefaultTableModel tableModel;



    public OrderManagementFrame_old(Supplier supplier){


        this.loggedInSupplier = supplier;


        initFrame();

        initHeader();

        initSidebar();

        initDashboardCards();


        setVisible(true);

    }





    private void initFrame(){


        setTitle("Grocery ERP - Order Management");


        setSize(1200,700);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setLocationRelativeTo(null);


        setLayout(new BorderLayout());


        getContentPane().setBackground(LIGHT_GREEN);


    }





    private void initHeader(){


        JPanel header = new JPanel();


        header.setBackground(DARK_GREEN);


        header.setPreferredSize(new Dimension(1200,80));


        header.setLayout(new FlowLayout(FlowLayout.LEFT,30,25));



        JLabel title = new JLabel("📦 Order Management");


        title.setForeground(Color.WHITE);


        title.setFont(new Font("Segoe UI",Font.BOLD,24));



        header.add(title);



        add(header,BorderLayout.NORTH);



    }





  private void initSidebar(){


        JPanel sidebar = new JPanel();


        sidebar.setBackground(SIDEBAR_BG);


        sidebar.setPreferredSize(new Dimension(230,600));


        sidebar.setLayout(
                new BoxLayout(sidebar,BoxLayout.Y_AXIS)
        );


        sidebar.setBorder(
                BorderFactory.createEmptyBorder(30,20,30,20)
        );



        createOrderBtn = createButton("➕ Create Order");


        viewOrdersBtn = createButton("📋 View Orders");

        viewOrdersBtn.addActionListener(e -> {

            loadOrders();
            loadStatistics();

        });



        searchOrderBtn = createButton("🔍 Search Order");


        searchOrderBtn.addActionListener(e -> {

            new SearchOrderFrame();

        });



        updateStatusBtn = createButton("✏ Update Status");


        updateStatusBtn.addActionListener(e -> {

            new UpdateOrderStatusFrame();

        });



        cancelOrderBtn = createButton("❌ Cancel Order");


        cancelOrderBtn.addActionListener(e -> {

            new CancelOrderFrame();

        });



        deleteOrderBtn = createButton("🗑 Delete Order");


        deleteOrderBtn.addActionListener(e -> {

            new DeleteOrderFrame();

        });



        backBtn = createButton("⬅ Back Dashboard");

        backBtn.addActionListener(e -> {

            dispose();

            new SupplierDashboard(loggedInSupplier);

        });


        createOrderBtn.addActionListener(e -> {

            new AddOrderFrame(loggedInSupplier);

        });





        sidebar.add(createOrderBtn);


        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );



        sidebar.add(viewOrdersBtn);


        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );



        sidebar.add(searchOrderBtn);


        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );



        sidebar.add(updateStatusBtn);


        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );



        sidebar.add(cancelOrderBtn);



        sidebar.add(
                Box.createRigidArea(
                        new Dimension(0,15)
                )
        );



        sidebar.add(deleteOrderBtn);



        sidebar.add(
                Box.createVerticalGlue()
        );



        sidebar.add(backBtn);



        add(sidebar,BorderLayout.WEST);


    }



    private JButton createButton(String text){


        JButton btn = new JButton(text);


        btn.setMaximumSize(new Dimension(190,45));


        btn.setBackground(BUTTON_GREEN);


        btn.setForeground(Color.WHITE);


        btn.setFont(
                new Font("Segoe UI",Font.BOLD,14)
        );


        return btn;

    }
 private void initDashboardCards() {


        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.setBackground(LIGHT_GREEN);



        // ================= CARDS =================


        JPanel cardsPanel = new JPanel(new GridLayout(1,4,20,20));

        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(20,20,20,20)
        );

        cardsPanel.setBackground(LIGHT_GREEN);



        JPanel totalCard = createCard(
                "Total Orders",
                BUTTON_GREEN
        );

        totalOrdersValue =
                (JLabel) totalCard.getClientProperty("value");



        JPanel pendingCard = createCard(
                "Pending",
                new Color(255,152,0)
        );

        pendingOrdersValue =
                (JLabel) pendingCard.getClientProperty("value");



        JPanel completedCard = createCard(
                "Completed",
                new Color(46,125,50)
        );

        completedOrdersValue =
                (JLabel) completedCard.getClientProperty("value");



        JPanel cancelledCard = createCard(
                "Cancelled",
                DELETE_RED
        );

        cancelledOrdersValue =
                (JLabel) cancelledCard.getClientProperty("value");



        cardsPanel.add(totalCard);
        cardsPanel.add(pendingCard);
        cardsPanel.add(completedCard);
        cardsPanel.add(cancelledCard);




        // ================= FILTER PANEL =================


        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        filterPanel.setBackground(LIGHT_GREEN);



        statusFilter = new JComboBox<>(
                new String[]{
                        "ALL",
                        "PENDING",
                        "COMPLETED",
                        "CANCELLED"
                }
        );


        paymentFilter = new JComboBox<>(
                new String[]{
                        "ALL",
                        "PAID",
                        "UNPAID"
                }
        );


        deliveryFilter = new JComboBox<>(
                new String[]{
                        "ALL",
                        "DELIVERED",
                        "NOT_DELIVERED",
                        "CANCELLED"
                }
        );



        applyFilterBtn = createButton("🔍 Apply Filter");



        applyFilterBtn.addActionListener(e -> {


            String status =
                    statusFilter.getSelectedItem().toString();


            String payment =
                    paymentFilter.getSelectedItem().toString();


            String delivery =
                    deliveryFilter.getSelectedItem().toString();



            filterOrders(
                    status,
                    payment,
                    delivery
            );


        });



        filterPanel.add(new JLabel("Status"));
        filterPanel.add(statusFilter);


        filterPanel.add(new JLabel("Payment"));
        filterPanel.add(paymentFilter);


        filterPanel.add(new JLabel("Delivery"));
        filterPanel.add(deliveryFilter);


        filterPanel.add(applyFilterBtn);




        // ================= CARD CLICK FILTER =================



        pendingCard.addMouseListener(
                new java.awt.event.MouseAdapter(){

            public void mouseClicked(
                    java.awt.event.MouseEvent e){

                loadOrdersByStatus("PENDING");

            }

        });



        completedCard.addMouseListener(
                new java.awt.event.MouseAdapter(){

            public void mouseClicked(
                    java.awt.event.MouseEvent e){

                loadOrdersByStatus("COMPLETED");

            }

        });



        cancelledCard.addMouseListener(
                new java.awt.event.MouseAdapter(){

            public void mouseClicked(
                    java.awt.event.MouseEvent e){

                loadOrdersByStatus("CANCELLED");

            }

        });



        totalCard.addMouseListener(
                new java.awt.event.MouseAdapter(){

            public void mouseClicked(
                    java.awt.event.MouseEvent e){

                loadOrders();

            }

        });




        // ================= TOP AREA =================


        JPanel topPanel =
                new JPanel(new BorderLayout());


        topPanel.setBackground(LIGHT_GREEN);


        topPanel.add(
                cardsPanel,
                BorderLayout.NORTH
        );


        topPanel.add(
                filterPanel,
                BorderLayout.SOUTH
        );



        centerPanel.add(
                topPanel,
                BorderLayout.NORTH
        );




        // ================= TABLE =================


        tableModel = new DefaultTableModel(

                new String[]{
                        "ID",
                        "Product ID",
                        "Supplier ID",
                        "Customer",
                        "Quantity",
                        "Amount",
                        "Status",
                        "Payment",
                        "Delivery",
                        "Date"
                },
                0
        );



        orderTable = new JTable(tableModel);

        orderTable.addMouseListener(
                new java.awt.event.MouseAdapter() {


            public void mouseClicked(
                    java.awt.event.MouseEvent e) {


                if(e.getClickCount() == 2){


                    int row =
                            orderTable.getSelectedRow();


                    if(row != -1){


                        showOrderDetails(row);


                    }

                }

            }

        });

        orderTable.setRowHeight(28);



        JScrollPane scrollPane =
                new JScrollPane(orderTable);



        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );



        add(
                centerPanel,
                BorderLayout.CENTER
        );



        loadOrders();

        loadStatistics();

    }
        private void loadStatistics() {

            try {

                totalOrdersValue.setText(
                        String.valueOf(
                                orderDAO.getTotalOrders()
                        )
                );


                pendingOrdersValue.setText(
                        String.valueOf(
                                orderDAO.getPendingOrders()
                        )
                );


                completedOrdersValue.setText(
                        String.valueOf(
                                orderDAO.getCompletedOrders()
                        )
                );


                cancelledOrdersValue.setText(
                        String.valueOf(
                                orderDAO.getCancelledOrders()
                        )
                );


            } catch(SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error Loading Statistics : " + e.getMessage()
                );

            }
        }





        private void loadOrders() {

            try {

                // clear old table data
                tableModel.setRowCount(0);


                // fetch data from database
                List<Order> orderList = orderDAO.getAllOrders();



                for(Order order : orderList) {


                    tableModel.addRow(
                            new Object[]{

                                    order.getOrderId(),

                                    order.getProductId(),

                                    order.getSupplierId(),

                                    order.getCustomerName(),

                                    order.getQuantity(),

                                    order.getTotalAmount(),

                                    order.getStatus(),

                                    order.getPaymentStatus(),

                                    order.getDeliveryStatus(),

                                    order.getOrderDate()

                            }
                    );

                }


            } catch(SQLException e) {


                JOptionPane.showMessageDialog(
                        this,
                        "Error Loading Orders : " + e.getMessage()
                );


            }

      
}
        private JPanel createCard(String title, Color color) {

            JPanel panel = new JPanel();

            panel.setLayout(
                    new BoxLayout(panel, BoxLayout.Y_AXIS)
            );

            panel.setBackground(Color.WHITE);

            panel.setBorder(
                    BorderFactory.createEmptyBorder(
                            15, 15, 15, 15
                    )
            );


            JLabel value = new JLabel("0");

            value.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            28
                    )
            );

            value.setForeground(color);



            JLabel label = new JLabel(title);

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );


            panel.add(value);

            panel.add(
                    Box.createRigidArea(
                            new Dimension(0,10)
                    )
            );

            panel.add(label);


            panel.putClientProperty(
                    "value",
                    value
            );


            return panel;
        }
        
        private void loadOrdersByStatus(String status){

            try{

                List<Order> orders =
                        orderDAO.getOrdersByStatus(status);


                tableModel.setRowCount(0);


                for(Order order : orders){

                    tableModel.addRow(new Object[]{

                            order.getOrderId(),
                            order.getProductId(),
                            order.getSupplierId(),
                            order.getCustomerName(),
                            order.getQuantity(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getPaymentStatus(),
                            order.getDeliveryStatus(),
                            order.getOrderDate()

                    });

                }


            }
            catch(Exception e){

                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage()
                );

            }

        }
        private void filterOrders(
                String status,
                String payment,
                String delivery
        ) {

            // Pehle table clear karo
            tableModel.setRowCount(0);


            try {

                List<Order> orders = orderDAO.getAllOrders();


                for(Order order : orders) {


                    boolean statusMatch =
                            status.equals("ALL") ||
                            status.equalsIgnoreCase(order.getStatus());


                    boolean paymentMatch =
                            payment.equals("ALL") ||
                            payment.equalsIgnoreCase(order.getPaymentStatus());


                    boolean deliveryMatch =
                            delivery.equals("ALL") ||
                            delivery.equalsIgnoreCase(order.getDeliveryStatus());



                    if(statusMatch && paymentMatch && deliveryMatch) {


                        tableModel.addRow(
                                new Object[]{

                                        order.getOrderId(),
                                        order.getProductId(),
                                        order.getSupplierId(),
                                        order.getCustomerName(),
                                        order.getQuantity(),
                                        order.getTotalAmount(),
                                        order.getStatus(),
                                        order.getPaymentStatus(),
                                        order.getDeliveryStatus(),
                                        order.getOrderDate()

                                }
                        );

                    }

                }


            }
            catch(Exception e){

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Error loading filtered orders"
                );

            }

       
        }

        
        
        private void showOrderDetails(int row){


            String details =

                    "Order ID : " 
                    + orderTable.getValueAt(row,0)

                    + "\nProduct ID : "
                    + orderTable.getValueAt(row,1)

                    + "\nSupplier ID : "
                    + orderTable.getValueAt(row,2)

                    + "\nCustomer : "
                    + orderTable.getValueAt(row,3)

                    + "\nQuantity : "
                    + orderTable.getValueAt(row,4)

                    + "\nAmount : "
                    + orderTable.getValueAt(row,5)

                    + "\nStatus : "
                    + orderTable.getValueAt(row,6)

                    + "\nPayment : "
                    + orderTable.getValueAt(row,7)

                    + "\nDelivery : "
                    + orderTable.getValueAt(row,8)

                    + "\nDate : "
                    + orderTable.getValueAt(row,9);



            JOptionPane.showMessageDialog(
                    this,
                    details,
                    "Order Details",
                    JOptionPane.INFORMATION_MESSAGE
            );


        }
}
		
		
