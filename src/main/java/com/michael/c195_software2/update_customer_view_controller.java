package com.michael.c195_software2;

import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class update_customer_view_controller {
    @FXML
    public Button save;
    @FXML
    public Button cancel;
    @FXML
    private TextField nameTextFLD;
    @FXML
    private TextField addressTextFLD;
    @FXML
    private TextField postalTextFLD;
    @FXML
    private TextField phoneTextFLD;

    Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to cancel?", ButtonType.YES, ButtonType.NO);

    private Customers selected;
    public void populate(Customers selected){
        //Pull data in
        this.selected = selected;
        int id = selected.getCustomerID();
        String name = selected.getCustomerName();
        String address = selected.getAddress();
        String postal = selected.getPostalCode();
        String phone = selected.getPhone();

        //assign data to fields
        nameTextFLD.setText(name);
        addressTextFLD.setText(address);
        postalTextFLD.setText(postal);
        phoneTextFLD.setText(phone);


    }

    public void cancel(ActionEvent actionEvent) throws IOException {
        cancelAlert.showAndWait();
        if(cancelAlert.getResult() == ButtonType.YES){
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        else{
            System.out.println("Why push it then?");
        }
    }

    public void save(ActionEvent actionEvent) throws SQLException {
        ObservableList<Integer> ID = FXCollections.observableArrayList();
        Customers newCustomer = new Customers();
        int custID = 0;
        //Collect ID
        String query = "SELECT Customer_ID FROM customers WHERE Customer_Name = '" + nameTextFLD.getText() + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            custID = rs.getInt("Customer_ID");

        }

        //TODO: Collect which user is logged in so we can add the changed by.
        String insertQuery = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?";
        PreparedStatement insertPS = InitCon.connection.prepareStatement(insertQuery);
        insertPS.setInt(1,custID);
        insertPS.setString(2,nameTextFLD.getText());
//        insertPS.setString(3,addressTextFLD.getText());
//        insertPS.setString(4, postalTextFLD.getText());
//        insertPS.setString(5,phoneTextFLD.getText());
//        insertPS.setString(7,"i need to fix this to collect user");
//        insertPS.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
//        insertPS.setString(9,"I need to fix this");
        insertPS.executeUpdate();

        //error Duplicate entry '0' for key

    }
}
