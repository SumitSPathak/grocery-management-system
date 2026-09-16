package com.grocery.dao.impl;


import com.grocery.dao.SupplierDAO;
import com.grocery.model.Supplier;
import com.grocery.util.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;



public class SupplierDAOImpl implements SupplierDAO {



    @Override
    public boolean addSupplier(Supplier supplier) throws SQLException {


        String sql =
                "INSERT INTO supplier " +
                "(supplier_name, company_name, phone, email, address, gst_number, username, password, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){



            stmt.setString(1, supplier.getSupplierName());

            stmt.setString(2, supplier.getCompanyName());

            stmt.setString(3, supplier.getPhone());

            stmt.setString(4, supplier.getEmail());

            stmt.setString(5, supplier.getAddress());

            stmt.setString(6, supplier.getGstNumber());

            stmt.setString(7, supplier.getUsername());

            stmt.setString(8, supplier.getPassword());

            stmt.setString(9, "ACTIVE");



            int rows = stmt.executeUpdate();


            return rows > 0;

        }

    }






    @Override
    public Supplier login(String username, String password) throws SQLException {


        String sql =
                "SELECT * FROM supplier WHERE username=? AND password=? AND status='ACTIVE'";


        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql)){


            stmt.setString(1, username);

            stmt.setString(2, password);



            ResultSet rs = stmt.executeQuery();



            if(rs.next()){


                Supplier supplier = new Supplier();


                supplier.setSupplierId(
                        rs.getInt("supplier_id")
                );


                supplier.setSupplierName(
                        rs.getString("supplier_name")
                );


                supplier.setCompanyName(
                        rs.getString("company_name")
                );


                supplier.setPhone(
                        rs.getString("phone")
                );


                supplier.setEmail(
                        rs.getString("email")
                );


                supplier.setAddress(
                        rs.getString("address")
                );


                supplier.setGstNumber(
                        rs.getString("gst_number")
                );


                // Login details
                supplier.setUsername(
                        rs.getString("username")
                );


                supplier.setPassword(
                        rs.getString("password")
                );


                supplier.setStatus(
                        rs.getString("status")
                );


                supplier.setCreatedAt(
                        rs.getTimestamp("created_at")
                );


                return supplier;

            }


        }


        return null;

    }







    @Override
    public Supplier getSupplierById(int supplierId) throws SQLException {


        String sql =
                "SELECT * FROM supplier WHERE supplier_id=?";



        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql)){



            stmt.setInt(1,supplierId);



            ResultSet rs = stmt.executeQuery();



            if(rs.next()){


                return mapResultSetToSupplier(rs);

            }

        }


        return null;

    }









    @Override
    public List<Supplier> getAllSuppliers() throws SQLException {



        List<Supplier> list = new ArrayList<>();


        String sql =
                "SELECT * FROM supplier ORDER BY supplier_id";



        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery()){



            while(rs.next()){


                list.add(
                        mapResultSetToSupplier(rs)
                );

            }

        }



        return list;

    }









    @Override
    public boolean updateSupplier(Supplier supplier) throws SQLException {



        String sql =
                "UPDATE supplier SET supplier_name=?, company_name=?, phone=?, email=?, address=?, gst_number=? WHERE supplier_id=?";



        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql)){



            stmt.setString(1,supplier.getSupplierName());

            stmt.setString(2,supplier.getCompanyName());

            stmt.setString(3,supplier.getPhone());

            stmt.setString(4,supplier.getEmail());

            stmt.setString(5,supplier.getAddress());

            stmt.setString(6,supplier.getGstNumber());

            stmt.setInt(7,supplier.getSupplierId());



            return stmt.executeUpdate()>0;

        }

    }









    @Override
    public boolean deleteSupplier(int supplierId) throws SQLException {



        String sql =
                "DELETE FROM supplier WHERE supplier_id=?";



        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql)){



            stmt.setInt(1,supplierId);


            return stmt.executeUpdate()>0;

        }

    }









    @Override
    public int getTotalSuppliers() throws SQLException {


        String sql =
                "SELECT COUNT(*) FROM supplier";


        try(Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery()){



            if(rs.next()){

                return rs.getInt(1);

            }

        }


        return 0;

    }









    private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {



        Supplier supplier = new Supplier();



        supplier.setSupplierId(
                rs.getInt("supplier_id")
        );


        supplier.setSupplierName(
                rs.getString("supplier_name")
        );


        supplier.setCompanyName(
                rs.getString("company_name")
        );


        supplier.setPhone(
                rs.getString("phone")
        );


        supplier.setEmail(
                rs.getString("email")
        );


        supplier.setAddress(
                rs.getString("address")
        );


        supplier.setGstNumber(
                rs.getString("gst_number")
        );


        supplier.setUsername(
                rs.getString("username")
        );


        supplier.setPassword(
                rs.getString("password")
        );


        supplier.setStatus(
                rs.getString("status")
        );


        supplier.setCreatedAt(
                rs.getTimestamp("created_at")
        );



        return supplier;

    }



}