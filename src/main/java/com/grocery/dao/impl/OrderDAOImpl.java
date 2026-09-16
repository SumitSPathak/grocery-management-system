package com.grocery.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grocery.dao.OrderDAO;
import com.grocery.model.Order;
import com.grocery.util.DBConnection;


public class OrderDAOImpl implements OrderDAO {


    @Override
    public boolean addOrder(Order order) throws SQLException {

        String sql = "INSERT INTO orders(product_id, supplier_id, customer_name, quantity, total_amount, status, payment_status, delivery_status) VALUES(?,?,?,?,?,?,?,?)";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setInt(1, order.getProductId());
            ps.setInt(2, order.getSupplierId());
            ps.setString(3, order.getCustomerName());
            ps.setInt(4, order.getQuantity());
            ps.setBigDecimal(5, order.getTotalAmount());
            ps.setString(6, order.getStatus());
            ps.setString(7, order.getPaymentStatus());
            ps.setString(8, order.getDeliveryStatus());


            return ps.executeUpdate() > 0;
        }
    }



    @Override
    public boolean updateOrder(Order order) throws SQLException {


        String sql = "UPDATE orders SET status=?, payment_status=?, delivery_status=? WHERE order_id=?";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, order.getStatus());
            ps.setString(2, order.getPaymentStatus());
            ps.setString(3, order.getDeliveryStatus());
            ps.setInt(4, order.getOrderId());


            return ps.executeUpdate() > 0;
        }

    }



    @Override
    public boolean deleteOrder(int orderId) throws SQLException {


        String sql = "DELETE FROM orders WHERE order_id=?";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;
        }

    }



    @Override
    public Order getOrderById(int orderId) throws SQLException {


        String sql = "SELECT * FROM orders WHERE order_id=?";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();


            if(rs.next()){

                return mapOrder(rs);

            }

        }


        return null;

    }



    @Override
    public List<Order> getAllOrders() throws SQLException {


        List<Order> list = new ArrayList<>();

        String sql = "SELECT * FROM orders ORDER BY order_id DESC";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){

            while(rs.next()){

                list.add(mapOrder(rs));

            }

        }


        return list;

    }



    @Override
    public List<Order> searchOrders(String keyword) throws SQLException {


        List<Order> list = new ArrayList<>();


        String sql = "SELECT * FROM orders WHERE customer_name LIKE ?";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, "%" + keyword + "%");


            ResultSet rs = ps.executeQuery();


            while(rs.next()){

                list.add(mapOrder(rs));

            }

        }


        return list;

    }




    @Override
    public List<Order> getOrdersByStatus(String status) throws SQLException {


        List<Order> list = new ArrayList<>();


        String sql = "SELECT * FROM orders WHERE status=?";


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, status);


            ResultSet rs = ps.executeQuery();


            while(rs.next()){

                list.add(mapOrder(rs));

            }

        }


        return list;

    }




    @Override
    public int getTotalOrders() throws SQLException {


        return getCount("SELECT COUNT(*) FROM orders");

    }



    @Override
    public int getPendingOrders() throws SQLException {


        return getCount("SELECT COUNT(*) FROM orders WHERE status='PENDING'");

    }




    @Override
    public int getCompletedOrders() throws SQLException {


        return getCount("SELECT COUNT(*) FROM orders WHERE status='COMPLETED'");

    }





    @Override
    public int getCancelledOrders() throws SQLException {


        return getCount("SELECT COUNT(*) FROM orders WHERE status='CANCELLED'");

    }




    private int getCount(String sql) throws SQLException {


        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){

            if(rs.next()){
                return rs.getInt(1);
            }

        }


        return 0;

    }





    private Order mapOrder(ResultSet rs) throws SQLException {


        Order order = new Order();


        order.setOrderId(rs.getInt("order_id"));

        order.setProductId(rs.getInt("product_id"));

        order.setSupplierId(rs.getInt("supplier_id"));

        order.setCustomerName(rs.getString("customer_name"));

        order.setQuantity(rs.getInt("quantity"));

        order.setTotalAmount(rs.getBigDecimal("total_amount"));

        order.setOrderDate(rs.getTimestamp("order_date"));

        order.setStatus(rs.getString("status"));

        order.setPaymentStatus(rs.getString("payment_status"));

        order.setDeliveryStatus(rs.getString("delivery_status"));



        return order;

    }
    @Override
    public List<Order> searchOrdersByCustomer(String name) {


        List<Order> list = new ArrayList<>();


        String sql =
        "SELECT * FROM orders WHERE customer_name LIKE ?";


        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {


            ps.setString(
                1,
                "%" + name + "%"
            );


            ResultSet rs = ps.executeQuery();


            while(rs.next()) {


                Order order = new Order();


                order.setOrderId(rs.getInt("order_id"));

                order.setProductId(rs.getInt("product_id"));

                order.setSupplierId(rs.getInt("supplier_id"));

                order.setCustomerName(
                    rs.getString("customer_name")
                );

                order.setQuantity(
                    rs.getInt("quantity")
                );


                order.setTotalAmount(
                    rs.getBigDecimal("total_amount")
                );


                order.setStatus(
                    rs.getString("status")
                );


                order.setPaymentStatus(
                    rs.getString("payment_status")
                );


                order.setDeliveryStatus(
                    rs.getString("delivery_status")
                );


                order.setOrderDate(
                    rs.getTimestamp("order_date")
                );


                list.add(order);

            }


        }
        catch(Exception e){

            e.printStackTrace();

        }


        return list;

    }
  @Override
public List<Order> getOrdersByCustomer(int customerId) throws SQLException {

    List<Order> list = new ArrayList<>();

    String sql =
        "SELECT o.* " +
        "FROM orders o " +
        "INNER JOIN customers c ON o.customer_name = c.customer_name " +
        "WHERE c.customer_id = ? " +
        "ORDER BY o.order_id DESC";

    try (
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setInt(1, customerId);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        }
    }

    return list;
}
}