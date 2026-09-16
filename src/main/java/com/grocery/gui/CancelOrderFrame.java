package com.grocery.gui;


import javax.swing.*;
import java.awt.*;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;
import com.grocery.model.Order;



public class CancelOrderFrame extends JFrame {


    private JTextField orderIdField;

    private JButton cancelBtn;
    private JButton backBtn;


    private OrderDAO orderDAO = new OrderDAOImpl();



    public CancelOrderFrame(){


        setTitle("Cancel Order");

        setSize(450,300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        initUI();


        setVisible(true);

    }





    private void initUI(){


        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(
                new Color(232,245,233)
        );


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);



        JLabel title = new JLabel("Cancel Order");


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );



        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;


        panel.add(title,gbc);




        gbc.gridwidth = 1;



        JLabel idLabel = new JLabel("Order ID");


        gbc.gridx = 0;
        gbc.gridy = 1;


        panel.add(idLabel,gbc);




        orderIdField = new JTextField(15);


        gbc.gridx = 1;


        panel.add(orderIdField,gbc);




        cancelBtn = new JButton("Cancel Order");


        backBtn = new JButton("Back");




        gbc.gridx = 0;
        gbc.gridy = 2;


        panel.add(cancelBtn,gbc);




        gbc.gridx = 1;


        panel.add(backBtn,gbc);




        add(panel);





        cancelBtn.addActionListener(
                e -> cancelOrder()
        );




        backBtn.addActionListener(
                e -> dispose()
        );



    }







    private void cancelOrder(){



        try{


            int orderId = Integer.parseInt(
                    orderIdField.getText()
            );




            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel Order ID : "
                    + orderId + "?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION
            );



            if(confirm != JOptionPane.YES_OPTION){

                return;

            }





            Order order = new Order();



            order.setOrderId(orderId);



            order.setStatus(
                    "CANCELLED"
            );


            order.setPaymentStatus(
                    "UNPAID"
            );


            order.setDeliveryStatus(
                    "CANCELLED"
            );





            boolean result =
                    orderDAO.updateOrder(order);






            if(result){



                JOptionPane.showMessageDialog(
                        this,
                        "Order Cancelled Successfully"
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
        catch(NumberFormatException e){


            JOptionPane.showMessageDialog(
                    this,
                    "Please Enter Valid Order ID"
            );


        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );


        }



    }



}