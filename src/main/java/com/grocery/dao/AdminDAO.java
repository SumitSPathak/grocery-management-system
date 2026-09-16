package com.grocery.dao;
import java.util.List;
import com.grocery.model.Admin;

/**
 * AdminDAO
 * --------
 * Data Access Object interface for the Admin module.
 * Declares the contract for all database operations related to admin
 * accounts. Implementation (JDBC-based) will be provided by AdminDAOImpl
 * in the next step.
 */
public interface AdminDAO {

    /**
     * Validates admin login credentials against the database.
     *
     * @param username admin's username
     * @param password admin's password
     * @return true if a matching admin record exists, false otherwise
     */
    boolean validateAdmin(String username, String password);

    /**
     * Inserts a new admin record into the database.
     *
     * @param admin Admin object containing the new admin's details
     * @return true if the insert was successful, false otherwise
     */
    boolean addAdmin(Admin admin);

    /**
     * Retrieves a single admin record by username.
     *
     * @param username the username to search for
     * @return the matching Admin object, or null if no match is found
     */
    Admin getAdminByUsername(String username);
    List<Admin> getAllAdmins();
    boolean updateAdmin(Admin admin);

    boolean deleteAdmin(int adminId);
   

}
