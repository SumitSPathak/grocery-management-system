package com.grocery.gui;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;
import com.grocery.model.Order;

import java.util.List;


public class SearchOrderFrame extends JFrame {


    private JTextField orderIdField;
    private JTextField customerField;


    private JButton searchBtn;
    private JButton clearBtn;
    private JButton backBtn;


    private JTable table;

    private DefaultTableModel tableModel;


    private OrderDAO orderDAO = new OrderDAOImpl();



    public SearchOrderFrame(){


        setTitle("Grocery ERP - Search Order");

        setSize(1000,500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());


        initUI();


        setVisible(true);

    }





    private void initUI(){


        JPanel topPanel = new JPanel();



        JLabel label = new JLabel("Order ID");


        orderIdField = new JTextField(8);



        JLabel customerLabel = new JLabel("Customer");


        customerField = new JTextField(12);



        searchBtn = new JButton("Search");


        clearBtn = new JButton("Clear");


        backBtn = new JButton("Back");



        topPanel.add(label);

        topPanel.add(orderIdField);


        topPanel.add(customerLabel);

        topPanel.add(customerField);



        topPanel.add(searchBtn);

        topPanel.add(clearBtn);

        topPanel.add(backBtn);



        add(topPanel,BorderLayout.NORTH);




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

                },0
        );



        table = new JTable(tableModel);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );





        searchBtn.addActionListener(e -> searchOrder());



        clearBtn.addActionListener(e -> {

            orderIdField.setText("");

            customerField.setText("");

            tableModel.setRowCount(0);

        });



        backBtn.addActionListener(e -> dispose());



    }







    private void searchOrder(){


        tableModel.setRowCount(0);



        try{


            String idText =
                    orderIdField.getText().trim();



            String customer =
                    customerField.getText().trim();



            // Customer name search

            if(!customer.isEmpty()){



                List<Order> orders =
                        orderDAO.searchOrdersByCustomer(customer);



                for(Order order : orders){



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



            // Order ID search

            else if(!idText.isEmpty()){



                Order order =
                        orderDAO.getOrderById(
                                Integer.parseInt(idText)
                        );



                if(order != null){



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
                else{


                    JOptionPane.showMessageDialog(
                            this,
                            "Order Not Found"
                    );


                }


            }
            else{


                JOptionPane.showMessageDialog(
                        this,
                        "Enter Order ID or Customer Name"
                );


            }




        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );


        }



    }



}