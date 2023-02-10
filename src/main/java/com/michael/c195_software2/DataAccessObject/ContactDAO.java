package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Contacts;
import com.michael.c195_software2.dataBaseConnection.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This method is used as a bridge between the Database and the Contact class.
 */
public class ContactDAO {

    /**
     * This method is used to return contact information.
     * @return
     * @throws SQLException
     */
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
