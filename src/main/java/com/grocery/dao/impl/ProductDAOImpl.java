package com.grocery.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grocery.dao.ProductDAO;
import com.grocery.model.Product;
import com.grocery.util.DBConnection;


public class ProductDAOImpl implements ProductDAO {


    @Override
    public boolean addProduct(Product product) throws SQLException {

        String sql =
        "INSERT INTO product(product_name, category, price, quantity, supplier, expiry_date) VALUES (?, ?, ?, ?, ?, ?)";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, product.getSupplier());
            statement.setDate(6, product.getExpiryDate());


            return statement.executeUpdate() > 0;
        }
    }



    @Override
    public boolean updateProduct(Product product) throws SQLException {


        String sql =
        "UPDATE product SET product_name=?, category=?, price=?, quantity=?, supplier=?, expiry_date=? WHERE product_id=?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, product.getSupplier());
            statement.setDate(6, product.getExpiryDate());
            statement.setInt(7, product.getProductId());


            return statement.executeUpdate() > 0;

        }
    }



    @Override
    public boolean deleteProduct(int productId) throws SQLException {


        String sql =
        "DELETE FROM product WHERE product_id=?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setInt(1, productId);


            return statement.executeUpdate() > 0;

        }

    }



    @Override
    public Product getProductById(int productId) throws SQLException {


        String sql =
        "SELECT * FROM product WHERE product_id=?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setInt(1, productId);


            ResultSet rs = statement.executeQuery();


            if(rs.next()){


                Product product = new Product();


                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSupplier(rs.getString("supplier"));
                product.setExpiryDate(rs.getDate("expiry_date"));
                product.setCreatedAt(rs.getTimestamp("created_at"));


                return product;

            }

        }


        return null;

    }




    @Override
    public List<Product> getAllProducts() throws SQLException {


        List<Product> list = new ArrayList<>();


        String sql =
        "SELECT * FROM product";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){


            while(rs.next()){


                Product product = new Product();


                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSupplier(rs.getString("supplier"));
                product.setExpiryDate(rs.getDate("expiry_date"));
                product.setCreatedAt(rs.getTimestamp("created_at"));


                list.add(product);

            }

        }


        return list;

    }
    @Override
    public List<Product> getProductsByLimit(int limit, int offset) throws SQLException {


        List<Product> list = new ArrayList<>();


        String sql =
                "SELECT * FROM product LIMIT ? OFFSET ?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){


            statement.setInt(1, limit);

            statement.setInt(2, offset);


            ResultSet rs = statement.executeQuery();



            while(rs.next()){


                Product product = new Product();


                product.setProductId(
                        rs.getInt("product_id")
                );


                product.setProductName(
                        rs.getString("product_name")
                );


                product.setCategory(
                        rs.getString("category")
                );


                product.setPrice(
                        rs.getBigDecimal("price")
                );


                product.setQuantity(
                        rs.getInt("quantity")
                );


                product.setSupplier(
                        rs.getString("supplier")
                );


                product.setExpiryDate(
                        rs.getDate("expiry_date")
                );


                product.setCreatedAt(
                        rs.getTimestamp("created_at")
                );


                list.add(product);

            }

        }


        return list;

    }





    @Override
    public int getTotalProducts() throws SQLException {


        String sql =
        "SELECT COUNT(*) FROM product";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){

            if(rs.next())
                return rs.getInt(1);

        }


        return 0;

    }




    @Override
    public int getTotalCategories() throws SQLException {


        String sql =
        "SELECT COUNT(DISTINCT category) FROM product";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){

            if(rs.next())
                return rs.getInt(1);

        }


        return 0;

    }




    @Override
    public int getLowStockCount() throws SQLException {


        String sql =
        "SELECT COUNT(*) FROM product WHERE quantity < 10";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){

            if(rs.next())
                return rs.getInt(1);

        }


        return 0;

    }





    @Override
    public int getOutOfStockCount() throws SQLException {


        String sql =
        "SELECT COUNT(*) FROM product WHERE quantity=0";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){

            if(rs.next())
                return rs.getInt(1);

        }


        return 0;

    }





    @Override
    public int getExpiredProductsCount() throws SQLException {


        String sql =
        "SELECT COUNT(*) FROM product WHERE expiry_date < CURDATE()";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){

            if(rs.next())
                return rs.getInt(1);

        }


        return 0;

    }




    @Override
    public List<Product> getProductsByStatus(String status) throws SQLException {

        return new ArrayList<>();

    }





    @Override
    public boolean updateStock(int productId, int quantity) throws SQLException {


        String sql =
        "UPDATE product SET quantity=? WHERE product_id=?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setInt(1, quantity);
            statement.setInt(2, productId);


            return statement.executeUpdate()>0;

        }

    }





    // 🔥 SEARCH PRODUCT ADDED

    @Override
    public List<Product> searchProducts(String keyword) throws SQLException {


        List<Product> list = new ArrayList<>();


        String sql =
        "SELECT * FROM product WHERE product_name LIKE ? OR category LIKE ?";



        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){


            statement.setString(1,"%"+keyword+"%");
            statement.setString(2,"%"+keyword+"%");


            ResultSet rs = statement.executeQuery();



            while(rs.next()){


                Product product = new Product();


                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setCategory(rs.getString("category"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSupplier(rs.getString("supplier"));
                product.setExpiryDate(rs.getDate("expiry_date"));


                list.add(product);

            }


        }


        return list;

    }

    @Override
    public List<Product> getProductsByPrice(double price) throws SQLException {


        List<Product> list = new ArrayList<>();


        String sql =
                "SELECT * FROM product WHERE price <= ?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ){

            statement.setDouble(1, price);


            ResultSet rs =
                    statement.executeQuery();


            while(rs.next()){


                Product product = new Product();


                product.setProductId(
                        rs.getInt("product_id")
                );

                product.setProductName(
                        rs.getString("product_name")
                );

                product.setCategory(
                        rs.getString("category")
                );

                product.setPrice(
                        rs.getBigDecimal("price")
                );


                list.add(product);

            }

        }


        return list;

    }
    @Override
    public List<Product> getProductsByCategory(String category) throws SQLException {


        List<Product> list = new ArrayList<>();


        String sql =
                "SELECT * FROM product WHERE category=?";


        try(
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ){


            statement.setString(1, category);


            ResultSet rs =
                    statement.executeQuery();



            while(rs.next()){


                Product product = new Product();


                product.setProductId(
                        rs.getInt("product_id")
                );


                product.setProductName(
                        rs.getString("product_name")
                );


                product.setCategory(
                        rs.getString("category")
                );


                product.setPrice(
                        rs.getBigDecimal("price")
                );


                product.setQuantity(
                        rs.getInt("quantity")
                );


                product.setSupplier(
                        rs.getString("supplier")
                );


                product.setExpiryDate(
                        rs.getDate("expiry_date")
                );


                product.setCreatedAt(
                        rs.getTimestamp("created_at")
                );


                list.add(product);

            }

        }


        return list;

    }}

