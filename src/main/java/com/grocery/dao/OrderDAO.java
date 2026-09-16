package com.grocery.dao;
import java.sql.SQLException;
import java.util.List;

import com.grocery.model.Order;

public interface OrderDAO {


    // Create Order
	
    boolean addOrder(Order order) throws SQLException;


    // Update Order Status
    boolean updateOrder(Order order) throws SQLException;


    // Delete Order
    boolean deleteOrder(int orderId) throws SQLException;


    // Get Order By ID
    Order getOrderById(int orderId) throws SQLException;


    // Get All Orders
    List<Order> getAllOrders() throws SQLException;


    // Search Orders
    List<Order> searchOrders(String keyword) throws SQLException;


    // Filter By Status
    List<Order> getOrdersByStatus(String status) throws SQLException;

    List<Order> searchOrdersByCustomer(String name);
    // Dashboard Counts
    List<Order> getOrdersByCustomer(int customerId) throws SQLException;

    int getTotalOrders() throws SQLException;


    int getPendingOrders() throws SQLException;


    int getCompletedOrders() throws SQLException;


    int getCancelledOrders() throws SQLException;

}