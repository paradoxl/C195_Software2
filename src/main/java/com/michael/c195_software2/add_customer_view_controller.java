package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CountryDAO;
import com.michael.c195_software2.DataAccessObject.FirstLevelDivisionDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class add_customer_view_controller implements Initializable {
    @FXML
    private TextField nameTextFLD;
    @FXML
    private TextField addressTextFLD;
    @FXML
    private TextField postalTextFLD;
    @FXML
    private TextField phoneTextFLD;
    @FXML
    private ComboBox<String> CBOX;
    @FXML
    private ComboBox<String> SBOX;
    FirstLevelDivisions FLD = new FirstLevelDivisions();




    public void save(ActionEvent actionEvent) throws SQLException, IOException {
        ObservableList<Customers> list = FXCollections.observableArrayList();
        Customers newCustomer = new Customers();
        String query = "SELECT Customer_ID FROM customers";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        int custID = 0;
       while (rs.next()){
           custID = rs.getInt("Customer_ID") + 2;
       }

        System.out.println(custID);
        String name = nameTextFLD.getText();
        String address = addressTextFLD.getText();
        String postal = postalTextFLD.getText();
        String phone = phoneTextFLD.getText();
        String createdBy = "The machines are alive";
        String updateBy= "Seriously the machines are alive...";

        newCustomer.setCustomerID(custID);
        newCustomer.setCustomerName(name);
        newCustomer.setAddress(address);
        newCustomer.setPostalCode(postal);
        newCustomer.setPhone(phone);

        String insertQuery = "INSERT INTO customers VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement insertPS = InitCon.connection.prepareStatement(insertQuery);
        insertPS.setInt(1,custID);
        insertPS.setString(2,nameTextFLD.getText());
        insertPS.setString(3,addressTextFLD.getText());
        insertPS.setString(4,postalTextFLD.getText());
        insertPS.setString(5,phoneTextFLD.getText());
        insertPS.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        insertPS.setString(7,createdBy);
        insertPS.setTimestamp(8,Timestamp.valueOf(LocalDateTime.now()));
        insertPS.setString(9,updateBy);
//        insertPS.setInt();
        // figure out division id

        insertPS.executeUpdate();



        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * brings the user back to the main screen.
     * @param actionEvent
     * @throws IOException
     */
    public void cancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is used to populate the drop-down menus.
     * Contains the first LAMBDA that is used to populate First Level Division dropdown data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        try {
            // lists
            ObservableList<Countries> country = CountryDAO.getCountries();
            ObservableList<String> countryVal = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivisions> FLD = FirstLevelDivisionDAO.getFLD();
            ObservableList<String> fldVAL = FXCollections.observableArrayList();

            //cycle each country add to country box
            country.stream().map(Countries::getCountry).forEach(countryVal::add);
            CBOX.setItems(countryVal);

            //LAMBDA Expression 1 used to populate the FLD drop down menu
            FLD.forEach(firstLevelDivision -> fldVAL.add(firstLevelDivision.getDivision()));
            SBOX.setItems(fldVAL);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
