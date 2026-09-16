package com.grocery.gui;

import javax.swing.*;
import java.awt.*;

public class LoginSelectionFrame extends JFrame {


    private static final Color DARK_GREEN = new Color(27,94,32);
    private static final Color LIGHT_GREEN = new Color(232,245,233);
    private static final Color BUTTON_GREEN = new Color(56,142,60);


    private JButton adminLoginBtn;
    private JButton employeeLoginBtn;
    private JButton customerLoginBtn;
    private JButton supplierLoginBtn;



    public LoginSelectionFrame(){

        initFrame();

        initHeader();

        initButtonPanel();

        initFooter();


        setVisible(true);

    }




    private void initFrame(){

        setTitle("Grocery Management System");
        setSize(900,700);

        setMinimumSize(
                new Dimension(600,500)
        );

        setResizable(true);

        setExtendedState(JFrame.NORMAL);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLayout(new BorderLayout());


        getContentPane()
                .setBackground(LIGHT_GREEN);

    }




    private void initHeader(){


        JPanel header = new JPanel();


        header.setBackground(DARK_GREEN);


        header.setPreferredSize(
                new Dimension(700,100)
        );


        header.setLayout(
                new BoxLayout(header,BoxLayout.Y_AXIS)
        );


        JLabel title =
                new JLabel(
                        "🛒 Grocery Management System",
                        SwingConstants.CENTER
                );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );


        title.setForeground(Color.WHITE);


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );



        JLabel sub =
                new JLabel(
                        "Select Login Type",
                        SwingConstants.CENTER
                );


        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );


        sub.setForeground(
                Color.WHITE
        );


        sub.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );



        header.add(Box.createVerticalStrut(15));

        header.add(title);

        header.add(Box.createVerticalStrut(10));

        header.add(sub);



        add(header,BorderLayout.NORTH);

    }





    private void initButtonPanel(){


        JPanel panel = new JPanel();


        panel.setBackground(
                LIGHT_GREEN
        );


        panel.setLayout(
                new GridLayout(
                        4,
                        1,
                        0,
                        25
                )
        );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        50,
                        150,
                        50,
                        150
                )
        );




        adminLoginBtn =
                createButton(
                        "🛡 Admin Login"
                );


        employeeLoginBtn =
                createButton(
                        "👷 Employee Login"
                );


        customerLoginBtn =
                createButton(
                        "🧑 Customer Login"
                );


        supplierLoginBtn =
                createButton(
                        "🚚 Supplier Login"
                );




        adminLoginBtn.addActionListener(e -> {

            new AdminLoginFrame();

            dispose();

        });



        supplierLoginBtn.addActionListener(e -> {

            new SupplierLoginFrame();

            dispose();

        });



        employeeLoginBtn.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee Login Coming Soon"
            );

        });



        customerLoginBtn.addActionListener(e -> {

            new CustomerLoginFrame();

            dispose();

        });




        panel.add(adminLoginBtn);

        panel.add(employeeLoginBtn);

        panel.add(customerLoginBtn);

        panel.add(supplierLoginBtn);



        add(panel,BorderLayout.CENTER);


    }







    private JButton createButton(String text){


        JButton btn =
                new JButton(text);


        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );


        btn.setBackground(
                BUTTON_GREEN
        );


        btn.setForeground(
                Color.WHITE
        );


        btn.setFocusPainted(false);



        btn.setPreferredSize(
                new Dimension(
                        300,
                        50
                )
        );



        return btn;

    }






    private void initFooter(){


        JPanel footer =
                new JPanel();


        footer.setBackground(
                DARK_GREEN
        );


        footer.setPreferredSize(
                new Dimension(
                        700,
                        35
                )
        );


        JLabel label =
                new JLabel(
                        "© 2026 Grocery Management System"
                );


        label.setForeground(
                Color.WHITE
        );


        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );


        footer.add(label);



        add(
                footer,
                BorderLayout.SOUTH
        );


    }





    public static void main(String[] args){

        SwingUtilities.invokeLater(
                LoginSelectionFrame::new
        );

    }


}