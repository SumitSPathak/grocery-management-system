package com.grocery.gui;


import com.grocery.dao.AdminDAO;
import com.grocery.dao.impl.AdminDAOImpl;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;



public class AdminLoginFrame extends JFrame {


    private static final Color DARK_GREEN =
            new Color(27,94,32);

    private static final Color LIGHT_GREEN =
            new Color(232,245,233);

    private static final Color BUTTON_GREEN =
            new Color(56,142,60);

    private static final Color GRAY =
            new Color(100,100,100);



    private final AdminDAO adminDAO =
            new AdminDAOImpl();



    private JTextField usernameField;

    private JPasswordField passwordField;

    private JCheckBox showPasswordCheckBox;


    private JButton loginBtn;

    private JButton resetBtn;

    private JButton backBtn;



    public AdminLoginFrame(){


        initFrame();

        initHeader();

        initLoginCard();

        initButtons();


        setVisible(true);

    }






    private void initFrame(){


        setTitle(
                "Grocery ERP - Admin Login"
        );


        setSize(
                900,
                650
        );


        setMinimumSize(
                new Dimension(
                        600,
                        500
                )
        );


        setLocationRelativeTo(null);


        setResizable(true);


        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        setLayout(
                new BorderLayout()
        );


        getContentPane()
                .setBackground(
                        LIGHT_GREEN
                );

    }






    private void initHeader(){


        JPanel header =
                new JPanel();


        header.setBackground(
                DARK_GREEN
        );


        header.setPreferredSize(
                new Dimension(
                        0,
                        100
                )
        );


        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );



        JLabel title =
                new JLabel(
                        "🛡 Admin Login",
                        SwingConstants.CENTER
                );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );


        title.setForeground(
                Color.WHITE
        );


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );



        JLabel sub =
                new JLabel(
                        "Grocery Management System",
                        SwingConstants.CENTER
                );


        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );


        sub.setForeground(
                Color.WHITE
        );


        sub.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );



        header.add(
                Box.createVerticalStrut(15)
        );


        header.add(title);


        header.add(
                Box.createVerticalStrut(8)
        );


        header.add(sub);



        add(
                header,
                BorderLayout.NORTH
        );


    }









    private void initLoginCard(){


        JPanel main =
                new JPanel(
                        new GridBagLayout()
                );


        main.setBackground(
                LIGHT_GREEN
        );



        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );


        card.setBackground(
                Color.WHITE
        );


        card.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        50,
                        40,
                        50
                )
        );



        GridBagConstraints gbc =
                new GridBagConstraints();


        gbc.insets =
                new Insets(
                        15,
                        15,
                        15,
                        15
                );


        gbc.fill =
                GridBagConstraints.HORIZONTAL;



        Font labelFont =
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                );



        JLabel userLabel =
                new JLabel(
                        "Username"
                );


        userLabel.setFont(
                labelFont
        );



        gbc.gridx=0;
        gbc.gridy=0;


        card.add(
                userLabel,
                gbc
        );



        usernameField =
                new JTextField(
                        18
                );


        usernameField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );


        gbc.gridx=1;


        card.add(
                usernameField,
                gbc
        );






        JLabel passLabel =
                new JLabel(
                        "Password"
                );


        passLabel.setFont(
                labelFont
        );


        gbc.gridx=0;
        gbc.gridy=1;


        card.add(
                passLabel,
                gbc
        );



        passwordField =
                new JPasswordField(
                        18
                );


        passwordField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );



        gbc.gridx=1;


        card.add(
                passwordField,
                gbc
        );






        showPasswordCheckBox =
                new JCheckBox(
                        "Show Password"
                );


        showPasswordCheckBox.setBackground(
                Color.WHITE
        );



        gbc.gridx=1;
        gbc.gridy=2;


        card.add(
                showPasswordCheckBox,
                gbc
        );



        showPasswordCheckBox.addActionListener(e -> {


            if(showPasswordCheckBox.isSelected()){


                passwordField.setEchoChar(
                        (char)0
                );


            }
            else{


                passwordField.setEchoChar(
                        '•'
                );

            }

        });





        main.add(card);


        add(
                main,
                BorderLayout.CENTER
        );


    }








    private void initButtons(){


        JPanel panel =
                new JPanel();


        panel.setBackground(
                LIGHT_GREEN
        );


        panel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        20,
                        25
                )
        );



        loginBtn =
                createButton(
                        "Login",
                        BUTTON_GREEN
                );


        resetBtn =
                createButton(
                        "Reset",
                        GRAY
                );


        backBtn =
                createButton(
                        "⬅ Back",
                        GRAY
                );



        loginBtn.addActionListener(
                this::handleLogin
        );


        resetBtn.addActionListener(
                e -> resetForm()
        );


        backBtn.addActionListener(
                e -> {

                    new LoginSelectionFrame();

                    dispose();

                }
        );



        panel.add(loginBtn);

        panel.add(resetBtn);

        panel.add(backBtn);



        add(
                panel,
                BorderLayout.SOUTH
        );


    }









    private JButton createButton(
            String text,
            Color color
    ){


        JButton btn =
                new JButton(
                        text
                );


        btn.setPreferredSize(
                new Dimension(
                        140,
                        45
                )
        );


        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );


        btn.setBackground(
                color
        );


        btn.setForeground(
                Color.WHITE
        );


        btn.setFocusPainted(
                false
        );


        return btn;

    }









    private void handleLogin(ActionEvent e){


        String username =
                usernameField.getText()
                        .trim();


        String password =
                new String(
                        passwordField.getPassword()
                ).trim();




        if(username.isEmpty()
                ||
                password.isEmpty()){


            JOptionPane.showMessageDialog(
                    this,
                    "Enter Username and Password"
            );


            return;

        }



        boolean result =
                adminDAO.validateAdmin(
                        username,
                        password
                );



        if(result){


            JOptionPane.showMessageDialog(
                    this,
                    "Login Successful"
            );


            new MainDashboard();


            dispose();


        }
        else{


            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password"
            );


            passwordField.setText("");

        }


    }







    private void resetForm(){


        usernameField.setText("");

        passwordField.setText("");

        showPasswordCheckBox.setSelected(false);

        passwordField.setEchoChar('•');


    }


}