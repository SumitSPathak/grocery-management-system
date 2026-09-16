package com.grocery.gui;


import javax.swing.*;
import java.awt.*;

import com.grocery.dao.CustomerDAO;
import com.grocery.dao.impl.CustomerDAOImpl;
import com.grocery.model.Customer;



public class CustomerRegisterFrame extends JFrame {


    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField usernameField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;


    private JButton registerBtn;
    private JButton clearBtn;
    private JButton backBtn;



    private CustomerDAO customerDAO = new CustomerDAOImpl();





    public CustomerRegisterFrame(){


        initFrame();

        initUI();

        setVisible(true);

    }







    private void initFrame(){


        setTitle("Customer Registration");

        setSize(600,650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

    }







    private void initUI(){



        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(232,245,233));



        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8,8,8,8);

        gbc.fill = GridBagConstraints.HORIZONTAL;



        JLabel title =
                new JLabel("Create Customer Account");


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



        nameField =
                addField(panel,gbc,1,"Customer Name");



        phoneField =
                addField(panel,gbc,2,"Phone Number");



        emailField =
                addField(panel,gbc,3,"Email");



        addressField =
                addField(panel,gbc,4,"Address");



        usernameField =
                addField(panel,gbc,5,"Username");





        JLabel passLabel =
                new JLabel("Password");

        gbc.gridx=0;
        gbc.gridy=6;

        panel.add(passLabel,gbc);



        passwordField =
                new JPasswordField(20);


        gbc.gridx=1;

        panel.add(passwordField,gbc);







        JLabel confirmLabel =
                new JLabel("Confirm Password");


        gbc.gridx=0;
        gbc.gridy=7;

        panel.add(confirmLabel,gbc);




        confirmPasswordField =
                new JPasswordField(20);


        gbc.gridx=1;

        panel.add(confirmPasswordField,gbc);







        registerBtn =
                new JButton("Register");


        clearBtn =
                new JButton("Clear");


        backBtn =
                new JButton("Back");




        JPanel buttonPanel =
                new JPanel();



        buttonPanel.add(registerBtn);

        buttonPanel.add(clearBtn);

        buttonPanel.add(backBtn);





        add(panel,BorderLayout.CENTER);

        add(buttonPanel,BorderLayout.SOUTH);





        registerBtn.addActionListener(e -> registerCustomer());



        clearBtn.addActionListener(e -> clearFields());



        backBtn.addActionListener(e -> {

            dispose();

            new CustomerLoginFrame();

        });



    }








    private JTextField addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText){



        JLabel label =
                new JLabel(labelText);



        gbc.gridx=0;

        gbc.gridy=row;


        panel.add(label,gbc);




        JTextField field =
                new JTextField(20);



        gbc.gridx=1;


        panel.add(field,gbc);



        return field;

    }









    private void registerCustomer(){



        try{



            String name =
                    nameField.getText().trim();



            String phone =
                    phoneField.getText().trim();



            String email =
                    emailField.getText().trim();



            String address =
                    addressField.getText().trim();



            String username =
                    usernameField.getText().trim();



            String password =
                    new String(
                    passwordField.getPassword()
                    ).trim();



            String confirmPassword =
                    new String(
                    confirmPasswordField.getPassword()
                    ).trim();







            // Mandatory validation

            if(name.isEmpty() ||
               phone.isEmpty() ||
               email.isEmpty() ||
               address.isEmpty() ||
               username.isEmpty() ||
               password.isEmpty() ||
               confirmPassword.isEmpty()){



                JOptionPane.showMessageDialog(
                        this,
                        "All fields are mandatory!"
                );


                return;

            }







            // Phone validation

            if(!phone.matches("\\d{10}")){


                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid 10 digit phone number"
                );


                return;

            }






            // Email validation

            if(!email.contains("@")){


                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid email"
                );


                return;

            }








            // Password match

            if(!password.equals(confirmPassword)){


                JOptionPane.showMessageDialog(
                        this,
                        "Password and Confirm Password do not match"
                );


                return;

            }








            Customer customer =
                    new Customer(
                            name,
                            phone,
                            email,
                            address,
                            username,
                            password
                    );





            boolean result =
                    customerDAO.addCustomer(customer);





            if(result){


                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful! Please Login"
                );


                dispose();

                new CustomerLoginFrame();


            }

            else{


                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed"
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







    private void clearFields(){


        nameField.setText("");

        phoneField.setText("");

        emailField.setText("");

        addressField.setText("");

        usernameField.setText("");

        passwordField.setText("");

        confirmPasswordField.setText("");

    }


}