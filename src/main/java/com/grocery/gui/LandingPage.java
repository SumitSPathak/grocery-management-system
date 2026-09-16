package com.grocery.gui;

import javax.swing.*;
import java.awt.*;


public class LandingPage extends JFrame {


    private static final Color DARK_GREEN = new Color(27,94,32);
    private static final Color LIGHT_GREEN = new Color(235,245,235);
    private static final Color BUTTON_GREEN = new Color(56,142,60);



    public LandingPage(){


        setTitle("Grocery Management System");

        setSize(1200,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBackground(LIGHT_GREEN);




        // HEADER

        JPanel header = new JPanel();

        header.setBackground(DARK_GREEN);

        header.setPreferredSize(
                new Dimension(1200,120)
        );


        JLabel title = new JLabel(
                "🛒 Grocery Management System",
                SwingConstants.CENTER
        );


        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );


        header.add(title);



        panel.add(
                header,
                BorderLayout.NORTH
        );





        // CENTER BUTTONS

        JPanel buttonPanel = new JPanel();
        JPanel mainCenter = new JPanel();
        mainCenter.setLayout(new BorderLayout());
        mainCenter.setOpaque(false);

        buttonPanel.setLayout(
                new GridLayout(
                        3,
                        1,
                        20,
                        20
                )
        );


        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        100,
                        350,
                        100,
                        350
                )
        );


        buttonPanel.setOpaque(false);





        JButton loginBtn =
                createButton(
                        "🔐 Login"
                );


        JButton signupBtn =
                createButton(
                        "📝 Create Account"
                );


        JButton guestBtn =
                createButton(
                        "👤 Continue as Guest"
                );




        buttonPanel.add(loginBtn);

        buttonPanel.add(signupBtn);

        buttonPanel.add(guestBtn);



        mainCenter.add(
                buttonPanel,
                BorderLayout.NORTH
        );


        panel.add(
                mainCenter,
                BorderLayout.CENTER
        );



        // LOGIN

        loginBtn.addActionListener(e -> {


            new CommonLoginFrame();


            dispose();


        });






        // SIGNUP

        signupBtn.addActionListener(e -> {


            new CustomerRegisterFrame();


            dispose();


        });







     // GUEST MODE

        guestBtn.addActionListener(e -> {

            new GuestProductFrame();

            dispose();

        });



        add(panel);


        setVisible(true);


    }






    private JButton createButton(String text){


        JButton btn =
                new JButton(text);


        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );


        btn.setBackground(BUTTON_GREEN);


        btn.setForeground(Color.WHITE);


        btn.setFocusPainted(false);



        return btn;

    }







    public static void main(String[] args){


        SwingUtilities.invokeLater(() -> {


            new LandingPage();


        });


    }



}