package com.grocery.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.math.BigDecimal;

import com.grocery.model.Order;
import com.grocery.model.Supplier;
import com.grocery.model.Product;

import com.grocery.dao.OrderDAO;
import com.grocery.dao.impl.OrderDAOImpl;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;


public class AddOrderFrame extends JFrame {


    private JComboBox<String> productComboBox;

    private JTextField customerField;
    private JTextField quantityField;
    private JTextField amountField;


    private List<Product> productList;


    private ProductDAO productDAO = new ProductDAOImpl();

    private OrderDAO orderDAO = new OrderDAOImpl();


    private Supplier loggedInSupplier;


    private JButton saveBtn;
    private JButton backBtn;



    public AddOrderFrame(Supplier supplier){

        this.loggedInSupplier = supplier;

        initFrame();
        initUI();
        loadProducts();

        setVisible(true);

    }




    private void initFrame(){

        setTitle("Grocery ERP - Create Order");

        setSize(650,500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

    }





    private void initUI(){


        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(232,245,233));


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        gbc.fill = GridBagConstraints.HORIZONTAL;




        JLabel title = new JLabel("Create New Order");

        title.setFont(
                new Font("Segoe UI",
                Font.BOLD,
                22)
        );


        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;

        panel.add(title,gbc);



        gbc.gridwidth=1;



        JLabel productLabel = new JLabel("Product");


        gbc.gridx=0;
        gbc.gridy=1;

        panel.add(productLabel,gbc);



        productComboBox = new JComboBox<>();


        gbc.gridx=1;

        panel.add(productComboBox,gbc);





        customerField =
                addField(panel,gbc,2,"Customer Name");



        quantityField =
                addField(panel,gbc,3,"Quantity");



        amountField =
                addField(panel,gbc,4,"Total Amount");



        amountField.setEditable(false);



        saveBtn = new JButton("Save Order");

        backBtn = new JButton("Back");



        JPanel btnPanel = new JPanel();

        btnPanel.add(saveBtn);

        btnPanel.add(backBtn);



        add(panel,BorderLayout.CENTER);

        add(btnPanel,BorderLayout.SOUTH);





        productComboBox.addActionListener(e -> calculateAmount());



        quantityField.addKeyListener(new java.awt.event.KeyAdapter(){

            public void keyReleased(java.awt.event.KeyEvent e){

                calculateAmount();

            }

        });




        saveBtn.addActionListener(e -> saveOrder());




        backBtn.addActionListener(e -> {

            dispose();

        });



    }





    private JTextField addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String name){


        JLabel label = new JLabel(name);



        gbc.gridx=0;
        gbc.gridy=row;


        panel.add(label,gbc);



        JTextField field = new JTextField(15);



        gbc.gridx=1;

        panel.add(field,gbc);



        return field;

    }






    private void loadProducts(){


        try{


            productList = productDAO.getAllProducts();



            for(Product p : productList){


                productComboBox.addItem(

                        p.getProductName()
                        +" - ₹"
                        +p.getPrice()

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







    private void calculateAmount(){


        try{


            int index =
                    productComboBox.getSelectedIndex();



            if(index < 0)
                return;



            Product product =
                    productList.get(index);



            String qtyText =
                    quantityField.getText();



            if(qtyText.isEmpty()){

                amountField.setText("");

                return;

            }



            int qty =
                    Integer.parseInt(qtyText);



            BigDecimal total =
                    product.getPrice()
                    .multiply(
                        new BigDecimal(qty)
                    );



            amountField.setText(
                    total.toString()
            );



        }
        catch(Exception e){

            amountField.setText("");

        }


    }








    private void saveOrder(){


        try{


            int index =
                    productComboBox.getSelectedIndex();



            Product selectedProduct =
                    productList.get(index);




            int qty =
                    Integer.parseInt(
                            quantityField.getText()
                    );



            if(qty <=0){

                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid quantity"
                );

                return;

            }





            if(qty > selectedProduct.getQuantity()){


                JOptionPane.showMessageDialog(
                        this,
                        "Only "
                        +selectedProduct.getQuantity()
                        +" items available"
                );

                return;

            }






            Order order = new Order();



            order.setProductId(
                    selectedProduct.getProductId()
            );



            order.setSupplierId(
                    loggedInSupplier.getSupplierId()
            );



            order.setCustomerName(
                    customerField.getText()
            );



            order.setQuantity(qty);



            order.setTotalAmount(
                    new BigDecimal(
                            amountField.getText()
                    )
            );



            order.setStatus("PENDING");

            order.setPaymentStatus("UNPAID");

            order.setDeliveryStatus("NOT_DELIVERED");





            boolean result =
                    orderDAO.addOrder(order);





            if(result){


                int remainingStock =
                        selectedProduct.getQuantity() - qty;



                productDAO.updateStock(
                        selectedProduct.getProductId(),
                        remainingStock
                );



                JOptionPane.showMessageDialog(
                        this,
                        "Order Added Successfully"
                );


                dispose();

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