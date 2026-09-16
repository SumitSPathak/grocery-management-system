package com.grocery.gui;

import javax.swing.*;
import java.awt.*;


public class ProductFilterPanel extends JPanel {


    public JButton allBtn;
    public JButton under49;
    public JButton under99;
    public JButton under199;

    public JButton snacks;
    public JButton grains;
    public JButton dryFruits;
    public JButton dairy;
    public JButton beverages;


    public ProductFilterPanel(){


        setLayout(
            new FlowLayout(
                FlowLayout.LEFT,
                15,
                10
            )
        );


        setBackground(
            new Color(245,245,245)
        );


        allBtn =
            createButton("All Products");


        under49 =
            createButton("🔥 Under ₹49");


        under99 =
            createButton("🔥 Under ₹99");


        under199 =
            createButton("🔥 Under ₹199");



        snacks =
            createButton("🍪 Snacks");


        grains =
            createButton("🍚 Grains");


        dryFruits =
            createButton("🥜 Dry Fruits");


        dairy =
            createButton("🥛 Dairy");


        beverages =
            createButton("🥤 Beverages");



        add(allBtn);

        add(under49);

        add(under99);

        add(under199);


        add(snacks);

        add(grains);

        add(dryFruits);

        add(dairy);

        add(beverages);


    }



    private JButton createButton(String text){


        JButton btn =
            new JButton(text);


        btn.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                14
            )
        );


        return btn;

    }

}