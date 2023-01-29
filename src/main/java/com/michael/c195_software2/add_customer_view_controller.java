package com.michael.c195_software2;

import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class add_customer_view_controller {
    @FXML
    private TextField nameTextFLD;
    @FXML
    private TextField addressTextFLD;
    @FXML
    private TextField postalTextFLD;
    @FXML
    private TextField phoneTextFLD;


    public void save(ActionEvent actionEvent) throws SQLException, IOException {
        ObservableList<Customers> list = FXCollections.observableArrayList();
        Customers newCustomer = new Customers();
        //how to set customer id
//        String query = "SELECT Customer_ID FROM Customers";
//        PreparedStatement ps = InitCon.connection.prepareStatement(query);
//        ResultSet rs = ps.executeQuery();
        int custID = 0;
////        while (rs.next()){
////            custID = rs.getInt("Customer_ID") + 1;
////        }
        System.out.println(custID);
        String name = nameTextFLD.getText();
        String address = addressTextFLD.getText();
        String postal = postalTextFLD.getText();
        String phone = phoneTextFLD.getText();

//        newCustomer.setCustomerID(custID);
        newCustomer.setCustomerName(name);
        newCustomer.setAddress(address);
        newCustomer.setPostalCode(postal);
        newCustomer.setPhone(phone);


        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();

    }
    public void cancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }
}
