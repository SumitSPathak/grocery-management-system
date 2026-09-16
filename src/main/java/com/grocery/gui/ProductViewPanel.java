package com.grocery.gui;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class ProductViewPanel extends JPanel {

    // ============================================================
    // DATABASE
    // ============================================================

    private final ProductDAO productDAO;

    // ============================================================
    // UI
    // ============================================================

    private JPanel productPanel;
    private ProductFilterPanel filterPanel;
    private JLabel loadingLabel;
    private JButton loadMoreBtn;

    // ============================================================
    // FILTER STATE
    // ============================================================

    private double priceLimit = 0;
    private String selectedCategory = "";

    // ============================================================
    // PAGINATION
    // ============================================================

    private int currentOffset = 0;

    private static final int LIMIT = 50;

    // ============================================================
    // CART CALLBACK
    // ============================================================

    private final Consumer<Product> addToCartCallback;

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    public ProductViewPanel() {
        this(null);
    }

    public ProductViewPanel(Consumer<Product> addToCartCallback) {

        this.addToCartCallback = addToCartCallback;

        productDAO = new ProductDAOImpl();

        setLayout(new BorderLayout());

        setBackground(new Color(232, 245, 233));

        createFilterPanel();

        createProductArea();

        loadProducts();
    }

    // ============================================================
    // FILTER PANEL
    // ============================================================

    private void createFilterPanel() {

        filterPanel = new ProductFilterPanel();

        JPanel filterWrapper = new JPanel(new BorderLayout());

        filterWrapper.setBackground(new Color(245, 245, 245));

        JScrollPane filterScroll = new JScrollPane(
                filterPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        filterScroll.setBorder(null);

        filterScroll.setPreferredSize(
                new Dimension(0, 65)
        );

        filterScroll.getHorizontalScrollBar()
                .setUnitIncrement(20);

        filterWrapper.add(
                filterScroll,
                BorderLayout.CENTER
        );

        add(
                filterWrapper,
                BorderLayout.NORTH
        );

        // ========================================================
        // ALL PRODUCTS
        // ========================================================

        filterPanel.allBtn.addActionListener(e -> {

            priceLimit = 0;

            selectedCategory = "";

            refreshProducts();
        });

        // ========================================================
        // UNDER 49
        // ========================================================

        filterPanel.under49.addActionListener(e -> {

            priceLimit = 49;

            selectedCategory = "";

            refreshProducts();
        });

        // ========================================================
        // UNDER 99
        // ========================================================

        filterPanel.under99.addActionListener(e -> {

            priceLimit = 99;

            selectedCategory = "";

            refreshProducts();
        });

        // ========================================================
        // UNDER 199
        // ========================================================

        filterPanel.under199.addActionListener(e -> {

            priceLimit = 199;

            selectedCategory = "";

            refreshProducts();
        });

        // ========================================================
        // SNACKS
        // ========================================================

        filterPanel.snacks.addActionListener(e -> {

            selectedCategory = "Snacks";

            priceLimit = 0;

            refreshProducts();
        });

        // ========================================================
        // GRAINS
        // ========================================================

        filterPanel.grains.addActionListener(e -> {

            selectedCategory = "Grains";

            priceLimit = 0;

            refreshProducts();
        });

        // ========================================================
        // DRY FRUITS
        // ========================================================

        filterPanel.dryFruits.addActionListener(e -> {

            selectedCategory = "Dry Fruits";

            priceLimit = 0;

            refreshProducts();
        });

        // ========================================================
        // DAIRY
        // ========================================================

        filterPanel.dairy.addActionListener(e -> {

            selectedCategory = "Dairy";

            priceLimit = 0;

            refreshProducts();
        });

        // ========================================================
        // BEVERAGES
        // ========================================================

        filterPanel.beverages.addActionListener(e -> {

            selectedCategory = "Beverages";

            priceLimit = 0;

            refreshProducts();
        });
    }

    // ============================================================
    // PRODUCT AREA
    // ============================================================

    private void createProductArea() {

        productPanel = new JPanel();

        productPanel.setLayout(
                new GridLayout(
                        0,
                        3,
                        20,
                        20
                )
        );

        productPanel.setBackground(
                new Color(232, 245, 233)
        );

        productPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JScrollPane productScroll =
                new JScrollPane(productPanel);

        productScroll.setBorder(null);

        productScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        productScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(
                productScroll,
                BorderLayout.CENTER
        );

        // ========================================================
        // BOTTOM PANEL
        // ========================================================

        loadingLabel = new JLabel(
                "",
                SwingConstants.CENTER
        );

        loadingLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        loadMoreBtn = new JButton(
                "Load More Products"
        );

        loadMoreBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        loadMoreBtn.addActionListener(e -> {

            if (
                    priceLimit > 0
                    ||
                    !selectedCategory.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Load More is available for All Products only.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            loadProducts();
        });

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setBackground(
                new Color(245, 245, 245)
        );

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        bottomPanel.add(
                loadingLabel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                loadMoreBtn,
                BorderLayout.EAST
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    // ============================================================
    // REFRESH PRODUCTS
    // ============================================================

    public void refreshProducts() {

        productPanel.removeAll();

        currentOffset = 0;

        loadProducts();

        productPanel.revalidate();

        productPanel.repaint();
    }

    // ============================================================
    // LOAD PRODUCTS
    // ============================================================

    private void loadProducts() {

        loadingLabel.setText(
                "Loading Products..."
        );

        try {

            List<Product> products;

            // ====================================================
            // PRICE FILTER
            // ====================================================

            if (priceLimit > 0) {

                products =
                        productDAO.getProductsByPrice(
                                priceLimit
                        );
            }

            // ====================================================
            // CATEGORY FILTER
            // ====================================================

            else if (!selectedCategory.isEmpty()) {

                products =
                        productDAO.getProductsByCategory(
                                selectedCategory
                        );
            }

            // ====================================================
            // ALL PRODUCTS
            // ====================================================

            else {

                products =
                        productDAO.getProductsByLimit(
                                LIMIT,
                                currentOffset
                        );
            }

            // ====================================================
            // NO PRODUCTS
            // ====================================================

            if (
                    products == null
                    ||
                    products.isEmpty()
            ) {

                productPanel.removeAll();

                productPanel.setLayout(
                        new BorderLayout()
                );

                JLabel noProductsLabel =
                        new JLabel(
                                "No Products Found",
                                SwingConstants.CENTER
                        );

                noProductsLabel.setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                18
                        )
                );

                noProductsLabel.setForeground(
                        new Color(
                                100,
                                100,
                                100
                        )
                );

                productPanel.add(
                        noProductsLabel,
                        BorderLayout.CENTER
                );

                loadingLabel.setText("");

                productPanel.revalidate();

                productPanel.repaint();

                return;
            }

            // ====================================================
            // GRID LAYOUT
            // ====================================================

            productPanel.removeAll();

            productPanel.setLayout(
                    new GridLayout(
                            0,
                            3,
                            20,
                            20
                    )
            );

            // ====================================================
            // CREATE PRODUCT CARDS
            // ====================================================

            for (Product p : products) {

                productPanel.add(
                        createProductCard(p)
                );
            }

            // ====================================================
            // PAGINATION
            // ====================================================

            if (
                    priceLimit == 0
                    &&
                    selectedCategory.isEmpty()
            ) {

                currentOffset += LIMIT;
            }

        }
        catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load products.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        loadingLabel.setText("");

        productPanel.revalidate();

        productPanel.repaint();
    }

    // ============================================================
    // PRODUCT CARD
    // ============================================================

    private JPanel createProductCard(Product p) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        210,
                                        210
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(
                        220,
                        300
                )
        );

        // ========================================================
        // IMAGE
        // ========================================================

        JLabel imageLabel =
                new JLabel();

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setPreferredSize(
                new Dimension(
                        0,
                        130
                )
        );

        // ========================================================
        // LOAD PRODUCT IMAGE
        // ========================================================

        String imageName =
                getImageFileName(
                        p.getProductName()
                );

        ImageIcon productIcon =
                loadProductImage(
                        imageName
                );

        if (productIcon != null) {

            Image image =
                    productIcon
                            .getImage()
                            .getScaledInstance(
                                    130,
                                    110,
                                    Image.SCALE_SMOOTH
                            );

            imageLabel.setIcon(
                    new ImageIcon(image)
            );

            imageLabel.setText("");

        }
        else {

            imageLabel.setIcon(null);

            imageLabel.setText(
                    "No Image"
            );

            imageLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            imageLabel.setForeground(
                    new Color(
                            80,
                            80,
                            80
                    )
            );
        }

        card.add(
                imageLabel,
                BorderLayout.NORTH
        );

        // ========================================================
        // PRODUCT NAME
        // ========================================================

        JLabel nameLabel =
                new JLabel(
                        "<html><center>"
                                + safeText(
                                        p.getProductName()
                                )
                                + "</center></html>",
                        SwingConstants.CENTER
                );

        nameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        nameLabel.setForeground(
                new Color(
                        33,
                        37,
                        41
                )
        );

        // ========================================================
        // CATEGORY
        // ========================================================

        JLabel categoryLabel =
                new JLabel(
                        "Category: "
                                + safeText(
                                        p.getCategory()
                                ),
                        SwingConstants.CENTER
                );

        categoryLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        categoryLabel.setForeground(
                new Color(
                        100,
                        100,
                        100
                )
        );

        // ========================================================
        // PRICE
        // ========================================================

        JLabel priceLabel =
                new JLabel(
                        "₹ "
                                + p.getPrice(),
                        SwingConstants.CENTER
                );

        priceLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        priceLabel.setForeground(
                new Color(
                        27,
                        94,
                        32
                )
        );

        // ========================================================
        // STOCK
        // ========================================================

        JLabel stockLabel =
                new JLabel(
                        "Stock: "
                                + p.getQuantity(),
                        SwingConstants.CENTER
                );

        stockLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        stockLabel.setForeground(
                new Color(
                        110,
                        110,
                        110
                )
        );

        // ========================================================
        // ADD TO CART
        // ========================================================

        JButton cartButton =
                new JButton(
                        "🛒 Add To Cart"
                );

        cartButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        cartButton.setBackground(
                new Color(
                        56,
                        142,
                        60
                )
        );

        cartButton.setForeground(
                Color.WHITE
        );

        cartButton.setFocusPainted(
                false
        );

        cartButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        cartButton.addActionListener(e -> {

            if (
                    addToCartCallback != null
            ) {

                addToCartCallback.accept(
                        p
                );

            }
            else {

                JOptionPane.showMessageDialog(
                        this,
                        p.getProductName()
                                + " selected."
                );
            }
        });

        // ========================================================
        // INFORMATION PANEL
        // ========================================================

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                5,
                                5
                        )
                );

        infoPanel.setOpaque(false);

        infoPanel.add(
                nameLabel
        );

        infoPanel.add(
                categoryLabel
        );

        infoPanel.add(
                priceLabel
        );

        infoPanel.add(
                stockLabel
        );

        infoPanel.add(
                cartButton
        );

        card.add(
                infoPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    // ============================================================
    // IMAGE FILE NAME MAPPING
    // ============================================================

    private String getImageFileName(
            String productName
    ) {

        if (productName == null) {
            return null;
        }

        String name =
                productName
                        .trim()
                        .toLowerCase();

        // ========================================================
        // GRAINS / BASIC GROCERY
        // ========================================================

        if (name.equals("rice")) {
            return "Rice.jpg";
        }

        if (
                name.equals("wheat flour")
                ||
                name.equals("flour")
        ) {
            return "Flour.jpg";
        }

        if (name.equals("atta")) {
            return "Atta.jpg";
        }

        if (name.equals("dal")) {
            return "Dal.jpg";
        }

        if (
                name.equals("pulses")
                ||
                name.equals("pulse")
        ) {
            return "Pulses.jpg";
        }

        if (name.equals("salt")) {
            return "Salt.jpg";
        }

        // ========================================================
        // FRUITS
        // ========================================================

        if (name.equals("apple")) {
            return "Apple.jpg";
        }

        if (name.equals("banana")) {
            return "Banana.jpg";
        }

        if (name.equals("mango")) {
            return "Mango.jpg";
        }

        if (
                name.equals("raisins")
                ||
                name.equals("raisin")
        ) {
            return "Raisins.jpg";
        }

        // ========================================================
        // DRY FRUITS
        // ========================================================

        if (
                name.equals("almond")
                ||
                name.equals("almonds")
        ) {
            return "Almond.jpg";
        }

        if (
                name.equals("cashew")
                ||
                name.equals("cashews")
        ) {
            return "Cashew.jpg";
        }

        // ========================================================
        // DAIRY
        // ========================================================

        if (name.equals("milk")) {
            return "Milk.jpg";
        }

        if (name.equals("butter")) {
            return "Butter.jpg";
        }

        if (name.equals("cheese")) {
            return "Cheese.jpg";
        }
        if (name.equalsIgnoreCase("Sugar")) {
            return "Sugar.jpg";
        }

        if (
                name.equals("curd")
                ||
                name.equals("yogurt")
                ||
                name.equals("yoghurt")
        ) {
            return "Curd.jpg";
        }

        // ========================================================
        // BEVERAGES
        // ========================================================

        if (name.equals("tea")) {
            return "Tea.jpg";
        }

        if (name.equals("coffee")) {
            return "Coffee.jpg";
        }

        if (
                name.equals("cold drinks")
                ||
                name.equals("cold drink")
                ||
                name.equals("sprite")
                ||
                name.equals("cold drinks(sprite)")
        ) {
            return "Cold Drinks(Sprite).jpg";
        }

        if (name.equals("juice")) {
            return "Juice.jpg";
        }
       
        // ========================================================
        // SNACKS
        // ========================================================

        if (
                name.equals("biscuit")
                ||
                name.equals("biscuits")
        ) {
            return "Biscuits.jpg";
        }

        if (
                name.equals("chips")
                ||
                name.equals("chip")
        ) {
            return "Chips.jpg";
        }

        if (
                name.equals("namkeen")
        ) {
            return "Namkeen.jpg";
        }

        if (
                name.equals("snack")
                ||
                name.equals("snacks")
        ) {
            return "Snack.jpg";
        }

        // ========================================================
        // OTHER ITEMS
        // ========================================================

        if (name.equals("jam")) {
            return "Jam.jpg";
        }

        if (name.equals("tomato")) {
            return "Tomato.jpg";
        }

        if (
                name.equals("onion")
                ||
                name.equals("onions")
        ) {
            return "Onion.jpg";
        }

        if (
                name.equals("potato")
                ||
                name.equals("potatoes")
        ) {
            return "Potato.jpg";
        }

        if (
                name.equals("noodles")
                ||
                name.equals("noodle")
        ) {
            return "Noodles.jpg";
        }

        // ========================================================
        // UNKNOWN PRODUCT
        // ========================================================

        return null;
    }

    // ============================================================
    // LOAD IMAGE
    // ============================================================

    private ImageIcon loadProductImage(
            String imageName
    ) {

        if (
                imageName == null
                ||
                imageName.trim().isEmpty()
        ) {

            return null;
        }

        // ========================================================
        // METHOD 1: MAVEN CLASSPATH
        // ========================================================

        try {

            String resourcePath =
                    "/Images/"
                            + imageName;

            URL imageUrl =
                    getClass()
                            .getResource(
                                    resourcePath
                            );

            if (imageUrl != null) {

                System.out.println(
                        "IMAGE FOUND: "
                                + resourcePath
                );

                return new ImageIcon(
                        imageUrl
                );
            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        // ========================================================
        // METHOD 2: CLASS LOADER
        // ========================================================

        try {

            String resourcePath =
                    "Images/"
                            + imageName;

            URL imageUrl =
                    getClass()
                            .getClassLoader()
                            .getResource(
                                    resourcePath
                            );

            if (imageUrl != null) {

                System.out.println(
                        "IMAGE FOUND BY CLASSLOADER: "
                                + resourcePath
                );

                return new ImageIcon(
                        imageUrl
                );
            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        // ========================================================
        // METHOD 3: ECLIPSE / MAVEN FILE SYSTEM
        // ========================================================

        try {

            File file =
                    new File(
                            "src/main/resources/Images/"
                                    + imageName
                    );

            if (file.exists()) {

                System.out.println(
                        "IMAGE FOUND FROM FILE SYSTEM: "
                                + file.getAbsolutePath()
                );

                return new ImageIcon(
                        file.getAbsolutePath()
                );
            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        // ========================================================
        // IMAGE NOT FOUND
        // ========================================================

        System.out.println(
                "IMAGE NOT FOUND: "
                        + imageName
        );

        return null;
    }

    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safeText(
            String value
    ) {

        if (
                value == null
                ||
                value.trim().isEmpty()
        ) {

            return "N/A";
        }

        return value;
    }
}