package com.grocery.gui;


import javax.swing.*;
import java.awt.*;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;


public class DeleteOrderFrame extends JFrame {


    private JTextField orderIdField;

    private JButton deleteBtn;
    private JButton backBtn;


    private OrderDAO orderDAO = new OrderDAOImpl();



    public DeleteOrderFrame(){


        setTitle("Delete Order");

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


        GridBagConstraints gbc =
                new GridBagConstraints();


        gbc.insets =
                new Insets(10,10,10,10);



        JLabel title =
                new JLabel("Delete Order");


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


        panel.add(title,gbc);



        gbc.gridwidth=1;



        JLabel idLabel =
                new JLabel("Order ID");


        gbc.gridx=0;
        gbc.gridy=1;


        panel.add(idLabel,gbc);




        orderIdField =
                new JTextField(15);


        gbc.gridx=1;


        panel.add(orderIdField,gbc);




        deleteBtn =
                new JButton("Delete Order");


        backBtn =
                new JButton("Back");




        gbc.gridx=0;
        gbc.gridy=2;


        panel.add(deleteBtn,gbc);



        gbc.gridx=1;


        panel.add(backBtn,gbc);



        add(panel);




        deleteBtn.addActionListener(e -> deleteOrder());



        backBtn.addActionListener(e -> {

            dispose();

        });



    }


private void deleteOrder(){


        try{


            int orderId =
                    Integer.parseInt(
                            orderIdField.getText()
                    );



            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete Order ID : "
                    + orderId + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );



            if(confirm != JOptionPane.YES_OPTION){

                return;

            }





            boolean result =
                    orderDAO.deleteOrder(orderId);




            if(result){


                JOptionPane.showMessageDialog(
                        this,
                        "Order Deleted Successfully"
                );


                dispose();


            }
            else{


                JOptionPane.showMessageDialog(
                        this,
                        "Order Not Found"
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