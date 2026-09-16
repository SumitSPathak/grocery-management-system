package com.grocery.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import com.grocery.dao.ProductDAO;
import com.grocery.dao.impl.ProductDAOImpl;
import com.grocery.model.Product;

public class GuestProductFrame extends JFrame {

    private JPanel productPanel;
    private ProductDAO productDAO;
    private JLabel loadingLabel;
    private JButton loadMoreBtn;
    private ProductFilterPanel filterPanel;

    private int currentOffset = 0;
    private static final int LIMIT = 50;

    private double priceLimit = 0;
    private String selectedCategory = "";

    // =========================================================
    // NORMAL GUEST MODE
    // =========================================================

    public GuestProductFrame() {
        this(0);
    }

    // =========================================================
    // PRICE FILTER MODE
    // =========================================================

    public GuestProductFrame(double priceLimit) {

        this.priceLimit = priceLimit;

        productDAO = new ProductDAOImpl();

        setTitle("Grocery - Guest Mode");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        createProductArea();
        createHeader();
        loadProducts();

        setVisible(true);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(27, 94, 32));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
        );

        JLabel logo = new JLabel("🛒 Grocery");

        logo.setForeground(Color.WHITE);

        logo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        header.add(
                logo,
                BorderLayout.WEST
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        JButton loginBtn = new JButton("🔵 Login");

        loginBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        loginBtn.addActionListener(e -> {

            new CommonLoginFrame();

            dispose();
        });

        header.add(
                loginBtn,
                BorderLayout.EAST
        );

        // =====================================================
        // FILTER PANEL
        // =====================================================

        filterPanel = new ProductFilterPanel();

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.add(
                header,
                BorderLayout.NORTH
        );

        topPanel.add(
                filterPanel,
                BorderLayout.SOUTH
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // ALL PRODUCTS
        // =====================================================

        filterPanel.allBtn.addActionListener(e -> {

            priceLimit = 0;
            selectedCategory = "";

            refreshProducts();
        });

        // =====================================================
        // PRICE FILTERS
        // =====================================================

        filterPanel.under49.addActionListener(e -> {

            priceLimit = 49;
            selectedCategory = "";

            refreshProducts();
        });

        filterPanel.under99.addActionListener(e -> {

            priceLimit = 99;
            selectedCategory = "";

            refreshProducts();
        });

        filterPanel.under199.addActionListener(e -> {

            priceLimit = 199;
            selectedCategory = "";

            refreshProducts();
        });

        // =====================================================
        // CATEGORY FILTERS
        // =====================================================

        filterPanel.snacks.addActionListener(e -> {

            selectedCategory = "Snacks";
            priceLimit = 0;

            refreshProducts();
        });

        filterPanel.grains.addActionListener(e -> {

            selectedCategory = "Grains";
            priceLimit = 0;

            refreshProducts();
        });

        filterPanel.dryFruits.addActionListener(e -> {

            selectedCategory = "Dry Fruits";
            priceLimit = 0;

            refreshProducts();
        });

        filterPanel.dairy.addActionListener(e -> {

            selectedCategory = "Dairy";
            priceLimit = 0;

            refreshProducts();
        });

        filterPanel.beverages.addActionListener(e -> {

            selectedCategory = "Beverages";
            priceLimit = 0;

            refreshProducts();
        });
    }

    // =========================================================
    // PRODUCT AREA
    // =========================================================

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
                new Color(245, 245, 245)
        );

        productPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(productPanel);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // LOADING
        // =====================================================

        loadingLabel =
                new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        loadingLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        // =====================================================
        // LOAD MORE
        // =====================================================

        loadMoreBtn =
                new JButton(
                        "Load More Products"
                );

        loadMoreBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        loadMoreBtn.addActionListener(e -> {

            loadProducts();
        });

        // =====================================================
        // BOTTOM PANEL
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.add(
                loadingLabel,
                BorderLayout.CENTER
        );

        bottom.add(
                loadMoreBtn,
                BorderLayout.EAST
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // REFRESH PRODUCTS
    // =========================================================

    private void refreshProducts() {

        productPanel.removeAll();

        currentOffset = 0;

        loadMoreBtn.setEnabled(true);

        loadMoreBtn.setText(
                "Load More Products"
        );

        loadProducts();
    }

    // =========================================================
    // LOAD PRODUCTS
    // =========================================================

    private void loadProducts() {

        try {

            loadingLabel.setText(
                    "Loading Products..."
            );

            List<Product> products;

            // =================================================
            // PRICE FILTER
            // =================================================

            if (priceLimit > 0) {

                products =
                        productDAO.getProductsByPrice(
                                priceLimit
                        );
            }

            // =================================================
            // CATEGORY FILTER
            // =================================================

            else if (!selectedCategory.equals("")) {

                products =
                        productDAO.getProductsByCategory(
                                selectedCategory
                        );
            }

            // =================================================
            // ALL PRODUCTS
            // =================================================

            else {

                products =
                        productDAO.getProductsByLimit(
                                LIMIT,
                                currentOffset
                        );
            }

            // =================================================
            // NO PRODUCTS
            // =================================================

            if (products == null ||
                    products.isEmpty()) {

                loadingLabel.setText(
                        "No Products Found"
                );

                loadMoreBtn.setEnabled(false);

                productPanel.revalidate();
                productPanel.repaint();

                return;
            }

            // =================================================
            // CREATE CARDS
            // =================================================

            for (Product p : products) {

                JPanel card =
                        createProductCard(p);

                productPanel.add(card);
            }

            // =================================================
            // PAGINATION
            // =================================================

            if (priceLimit == 0 &&
                    selectedCategory.equals("")) {

                currentOffset += LIMIT;

                if (products.size() < LIMIT) {

                    loadMoreBtn.setEnabled(false);

                    loadMoreBtn.setText(
                            "No More Products"
                    );
                }
            }

            // =================================================
            // FILTER MODE
            // =================================================

            else {

                loadMoreBtn.setEnabled(false);

                loadMoreBtn.setText(
                        "Filter Applied"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Products Load Error\n"
                            + e.getMessage()
            );

        } finally {

            loadingLabel.setText("");

            productPanel.revalidate();
            productPanel.repaint();
        }
    }

    // =========================================================
    // CREATE PRODUCT CARD
    // =========================================================

    private JPanel createProductCard(Product p) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );

        // =====================================================
        // IMAGE PANEL
        // =====================================================

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
                        135
                )
        );

        imageLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        // =====================================================
        // LOAD PRODUCT IMAGE
        // =====================================================

        loadProductImage(
                imageLabel,
                p.getProductName()
        );

        card.add(
                imageLabel,
                BorderLayout.NORTH
        );

        // =====================================================
        // PRODUCT NAME
        // =====================================================

        JLabel name =
                new JLabel(
                        safeText(p.getProductName()),
                        SwingConstants.CENTER
                );

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        // =====================================================
        // CATEGORY
        // =====================================================

        JLabel category =
                new JLabel(
                        safeText(p.getCategory()),
                        SwingConstants.CENTER
                );

        category.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        // =====================================================
        // PRICE
        // =====================================================

        JLabel price =
                new JLabel(
                        "₹ " + p.getPrice(),
                        SwingConstants.CENTER
                );

        price.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        // =====================================================
        // CART BUTTON
        // =====================================================

        JButton cart =
                new JButton(
                        "🛒 Add To Cart"
                );

        cart.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        cart.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Please login to add products to cart."
            );

            new CommonLoginFrame();

            dispose();
        });

        // =====================================================
        // INFO PANEL
        // =====================================================

        JPanel info =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                5,
                                5
                        )
                );

        info.setBackground(Color.WHITE);

        info.add(name);
        info.add(category);
        info.add(price);
        info.add(cart);

        card.add(
                info,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // PRODUCT IMAGE LOADER
    // =========================================================

    private void loadProductImage(
            JLabel label,
            String productName
    ) {

        label.setText("No Image");

        if (productName == null ||
                productName.trim().isEmpty()) {

            return;
        }

        String imageName =
                getImageName(productName);

        if (imageName == null) {
            return;
        }

        try {

            // IMPORTANT:
            // Images folder is:
            // src/main/resources/Images

            String resourcePath =
                    "/Images/" + imageName;

            URL imageURL =
                    getClass().getResource(
                            resourcePath
                    );

            if (imageURL == null) {

                System.out.println(
                        "IMAGE NOT FOUND: "
                                + resourcePath
                );

                return;
            }

            ImageIcon originalIcon =
                    new ImageIcon(imageURL);

            Image originalImage =
                    originalIcon.getImage();

            Image scaledImage =
                    originalImage.getScaledInstance(
                            150,
                            110,
                            Image.SCALE_SMOOTH
                    );

            ImageIcon finalIcon =
                    new ImageIcon(
                            scaledImage
                    );

            label.setText("");

            label.setIcon(finalIcon);

            System.out.println(
                    "IMAGE LOADED: "
                            + resourcePath
            );

        } catch (Exception e) {

            System.out.println(
                    "IMAGE ERROR: "
                            + productName
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // IMAGE NAME MAPPING
    // =========================================================

    private String getImageName(
            String productName
    ) {

        String name =
                productName
                        .trim()
                        .toLowerCase();

        // =====================================================
        // GRAINS / GROCERY
        // =====================================================

        if (name.equals("rice")) {
            return "Rice.jpg";
        }

        if (name.equals("wheat flour")) {
            return "Flour.jpg";
        }

        if (name.equals("flour")) {
            return "Flour.jpg";
        }

        if (name.equals("atta")) {
            return "Atta.jpg";
        }

        if (name.equals("dal")) {
            return "Dal.jpg";
        }

        if (name.equals("sugar")) {
            return "Sugar.jpg";
        }

        if (name.equals("salt")) {
            return "Salt.jpg";
        }

        // =====================================================
        // DAIRY
        // =====================================================

        if (name.equals("milk")) {
            return "Milk.jpg";
        }

        if (name.equals("butter")) {
            return "Butter.jpg";
        }

        if (name.equals("cheese")) {
            return "Cheese.jpg";
        }

        if (name.equals("curd")) {
            return "Curd.jpg";
        }
        if (name.equalsIgnoreCase("Sugar")) {
            return "Sugar.jpg";
        }
        // =====================================================
        // BEVERAGES
        // =====================================================

        if (name.equals("tea")) {
            return "Tea.jpg";
        }

        if (name.equals("coffee")) {
            return "Coffee.jpg";
        }

        if (name.contains("cold drink")) {
            return "Cold Drinks(Sprite).jpg";
        }

        if (name.equals("juice")) {
            return "Juice.jpg";
        }

        // =====================================================
        // FRUITS
        // =====================================================

        if (name.equals("apple")) {
            return "Apple.jpg";
        }

        if (name.equals("banana")) {
            return "Banana.jpg";
        }

        if (name.equals("mango")) {
            return "Mango.jpg";
        }

        // =====================================================
        // DRY FRUITS
        // =====================================================

        if (name.equals("almond")) {
            return "Almond.jpg";
        }

        if (name.equals("cashew")) {
            return "Cashew.jpg";
        }

        // =====================================================
        // SNACKS
        // =====================================================

        if (name.equals("biscuit") ||
                name.equals("biscuits")) {

            return "Biscuits.jpg";
        }

        if (name.equals("chips")) {
            return "Chips.jpg";
        }

        if (name.equals("namkeen")) {
            return "Namkeen.jpg";
        }

        if (name.equals("noodles")) {
            return "Noodles.jpg";
        }

        if (name.equals("jam")) {
            return "Jam.jpg";
        }
        

        // =====================================================
        // GENERIC FALLBACK
        // =====================================================

        String[] possibleNames = {

                productName + ".jpg",

                productName.replace(
                        " ",
                        ""
                ) + ".jpg",

                productName.replace(
                        " ",
                        "_"
                ) + ".jpg"
        };

        for (String fileName : possibleNames) {

            URL url =
                    getClass().getResource(
                            "/Images/" + fileName
                    );

            if (url != null) {

                return fileName;
            }
        }

        return null;
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "N/A";
        }

        return value;
    }
}