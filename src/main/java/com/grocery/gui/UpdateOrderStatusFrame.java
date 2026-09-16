package com.grocery.gui;
import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;
import com.grocery.model.Order;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;


public class UpdateOrderStatusFrame extends JFrame {

	private OrderDAO orderDAO = new OrderDAOImpl();
    private JTextField orderIdField;

    private JComboBox<String> statusBox;
    private JButton loadBtn;
    private JComboBox<String> paymentBox;
    private JComboBox<String> deliveryBox;


    private JButton updateBtn;


    public UpdateOrderStatusFrame(){


        setTitle("Update Order Status");

        setSize(500,400);

        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        gbc.fill = GridBagConstraints.HORIZONTAL;



        JLabel title = new JLabel("Update Order Status");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );


        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;

        add(title,gbc);



        gbc.gridwidth=1;



        gbc.gridy=1;
        gbc.gridx=0;

        add(new JLabel("Order ID"),gbc);


        orderIdField = new JTextField(15);

        gbc.gridx=1;

        add(orderIdField,gbc);
        
        loadBtn = new JButton("Load");

        gbc.gridx=2;

        add(loadBtn,gbc);



        gbc.gridy=2;
        gbc.gridx=0;

        add(new JLabel("Status"),gbc);



        statusBox = new JComboBox<>(
                new String[]{
                        "PENDING",
                        "COMPLETED",
                        "CANCELLED"
                }
        );


        gbc.gridx=1;

        add(statusBox,gbc);



        gbc.gridy=3;
        gbc.gridx=0;

        add(new JLabel("Payment"),gbc);


        paymentBox = new JComboBox<>(
                new String[]{
                        "UNPAID",
                        "PAID"
                }
        );


        gbc.gridx=1;

        add(paymentBox,gbc);




        gbc.gridy=4;
        gbc.gridx=0;

        add(new JLabel("Delivery"),gbc);


        deliveryBox = new JComboBox<>(
                new String[]{
                        "NOT_DELIVERED",
                        "DELIVERED"
                }
        );


        gbc.gridx=1;

        add(deliveryBox,gbc);




        updateBtn = new JButton("Update");


        gbc.gridy=5;
        gbc.gridx=0;
        gbc.gridwidth=2;


        add(updateBtn,gbc);


        updateBtn.addActionListener(e -> updateOrder());
        loadBtn.addActionListener(e -> loadOrder());
        setVisible(true);



    }


    private void updateOrder(){

        try{

            Order order = new Order();


            order.setOrderId(
                    Integer.parseInt(
                            orderIdField.getText()
                    )
            );


            order.setStatus(
                    statusBox.getSelectedItem().toString()
            );


            order.setPaymentStatus(
                    paymentBox.getSelectedItem().toString()
            );


            order.setDeliveryStatus(
                    deliveryBox.getSelectedItem().toString()
            );



            boolean result = 
                    orderDAO.updateOrder(order);



            if(result){

                JOptionPane.showMessageDialog(
                        this,
                        "Order Updated Successfully"
                );

                dispose();

            }
            else{

                JOptionPane.showMessageDialog(
                        this,
                        "Order ID Not Found"
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
    private void loadOrder(){

        try{

            int id = Integer.parseInt(
                    orderIdField.getText()
            );


            Order order = 
                    orderDAO.getOrderById(id);



            if(order != null){


                statusBox.setSelectedItem(
                        order.getStatus()
                );


                paymentBox.setSelectedItem(
                        order.getPaymentStatus()
                );


                deliveryBox.setSelectedItem(
                        order.getDeliveryStatus()
                );


            }
            else{


                JOptionPane.showMessageDialog(
                        this,
                        "Order Not Found"
                );


            }


        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    "Enter Valid Order ID"
            );


        }

    }

    }




