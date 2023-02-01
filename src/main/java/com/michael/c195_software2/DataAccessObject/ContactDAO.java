package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Contacts;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {


    public static ObservableList<Contacts> getContacts() throws SQLException{
        ObservableList<Contacts> contacts = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println("Gathering contacts");
        while (rs.next()){
            Contacts add = new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email"));
            contacts.add(add);
        }
        return contacts;
    }
}
