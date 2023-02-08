package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Customers;

import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used as a bridge between the Database and the customer class.
 */
public class CustomerDAO {
    /**
     * This method is used to return customer information.
     * @return
     * @throws SQLException
     */
    public static ObservableList<Customers> getCustomers() throws SQLException {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM customers";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println("Query Launched");

        while(rs.next()){
            int customerid = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divID = rs.getInt("Division_ID");
            Customers customer = new Customers(customerid,name,address,postal,phone,divID);
            customerList.add(customer);
        }

        return customerList;
    }
}
