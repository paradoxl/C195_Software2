package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Users;
import com.michael.c195_software2.con.InitCon;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO {
//    public List<Users> getUser();
//    public Users getUser(int userID){
//        Users user = new Users();
//        return user;
//    }
//    public void updateUser(Users user);
//    public void deleteUser(Users user);

public void validation(String username, String password) throws SQLException {
    String query = "SELECT * FROM users WHERE User_name = '" + username + "' AND Password = '" + password + "'";
    PreparedStatement ps = InitCon.connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
//    while(rs.next()) {
//        System.out.println(rs.getString("User_Name"));
//    }
    while(rs.next()) {
        if (rs.getString("User_name").equals(username) && rs.getString("Password").equals(password)) {
            // connection successful.
            System.out.println("Match");
        }

    }
    }


}

