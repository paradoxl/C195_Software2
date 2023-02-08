package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Users;
import com.michael.c195_software2.con.InitCon;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is used as a bridge between the Database and the user class.
 */
public class UserDAO {

    /**
     * Validates a correct login.
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public Boolean validation(String username, String password) throws SQLException {
    String query = "SELECT * FROM users WHERE User_name = '" + username + "' AND Password = '" + password + "'";
    PreparedStatement ps = InitCon.connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();

    while(rs.next()) {
        if (rs.getString("User_name").equals(username) && rs.getString("Password").equals(password)) {
            // connection successful.
            System.out.println("Match");
            return true;
        }

    }
    return false;
    }



}

