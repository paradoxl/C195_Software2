package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CountryDAO;
import com.michael.c195_software2.DataAccessObject.FirstLevelDivisionDAO;
import com.michael.c195_software2.dataBaseConnection.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;
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
    @FXML
    private TextField hiddenVar;
    FirstLevelDivisions FLD = new FirstLevelDivisions();




    public void save(ActionEvent actionEvent) throws SQLException, IOException {

        if(nameTextFLD.getText().isEmpty()){
            Alert noName = new Alert(Alert.AlertType.ERROR,"You have not entered a name", ButtonType.OK);
            noName.showAndWait();
            return;
        }
        if(addressTextFLD.getText().isEmpty()){
            Alert noAddress = new Alert(Alert.AlertType.ERROR,"You have not entered an address",ButtonType.OK);
            noAddress.showAndWait();
            return;
        }
        if (postalTextFLD.getText().isEmpty()){
            Alert noPostal = new Alert(Alert.AlertType.ERROR,"You have not entered a postal code",ButtonType.OK);
            noPostal.showAndWait();
            return;
        }
        if(phoneTextFLD.getText().isEmpty()){
            Alert noPhone = new Alert(Alert.AlertType.ERROR, "You have not entered a phone number",ButtonType.OK);
            noPhone.showAndWait();
            return;
        }
        try {
            if(CBOX.getSelectionModel().getSelectedItem().isEmpty()){
                Alert noCountry = new Alert(Alert.AlertType.ERROR,"You have not selected a Country",ButtonType.OK);
                noCountry.showAndWait();
                return;
            }
        }
        catch (NullPointerException e){
            Alert noCountry = new Alert(Alert.AlertType.ERROR,"You have not selected a Country",ButtonType.OK);
            noCountry.showAndWait();
        }
        try{
            if(SBOX.getSelectionModel().getSelectedItem().isEmpty()){
                Alert noState = new Alert(Alert.AlertType.ERROR,"You have not selected a State/Prov",ButtonType.OK  );
                noState.showAndWait();
                return;
            }
        }
        catch (NullPointerException e){
            Alert noState = new Alert(Alert.AlertType.ERROR,"You have not selected a State/Prov",ButtonType.OK  );
            noState.showAndWait();
        }





        //TODO: BUG with customer id. +2 works for the first addition but not any others.
        ObservableList<Integer> list = FXCollections.observableArrayList();
        Customers newCustomer = new Customers();
        String query = "SELECT Customer_ID FROM customers";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        int custID = 3;


       while (rs.next()){
//           custID = rs.getInt("Customer_ID") + 1;
           list.add(rs.getInt("Customer_ID"));
       }
        Random ran = new Random();
       while (list.contains(custID)) {
           custID = ran.nextInt(100000);
       }
        String queryID = "SELECT Division_ID FROM first_level_divisions WHERE Division = '" + SBOX.getSelectionModel().getSelectedItem() + "'";
        PreparedStatement psID = InitCon.connection.prepareStatement(queryID);
        ResultSet rsID = psID.executeQuery();
        while (rsID.next()){
            hiddenVar.setText(String.valueOf(rsID.getInt("Division_ID")));
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

        String insertQuery = "INSERT INTO customers VALUES (?,?,?,?,?,?,?,?,?,?)";
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
        insertPS.setInt(10,Integer.parseInt(hiddenVar.getText()));
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
        Alert cancel = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to exit this page?",ButtonType.YES,ButtonType.NO);
        cancel.showAndWait();

        if(cancel.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
    }



    /**
     * This method is used to set up the combo boxes for the Country and State first level division data.
     * This method contains the first lambda used    "FLD.forEach((firstLevelDivisions -> fldCC.add(firstLevelDivisions.getCountryID())));"
     * This lambda is used in place of a loop.
     * @throws SQLException
     */
    public void dynamicCombo() throws SQLException {

//
//        // sets combo boxes
//        ObservableList<FirstLevelDivisions> FLD = FirstLevelDivisionDAO.getFLD();
//        ObservableList<String> fldVAL = FXCollections.observableArrayList();
//        ObservableList<Integer> fldCC = FXCollections.observableArrayList();
//        ObservableList<String> finalFLDVALUS = FXCollections.observableArrayList();
//        ObservableList<String> finalFLDVALCA = FXCollections.observableArrayList();
//        ObservableList<String> finalFLDVALUK = FXCollections.observableArrayList();
//        //First set of LAMBDA expressions used to gather data on FLD base on country id
//
//
//        CBOX.setOnAction( e ->{
//            FLD.forEach((firstLevelDivisions -> fldCC.add(firstLevelDivisions.getCountryID())));
//            FLD.forEach(firstLevelDivision -> fldVAL.add(firstLevelDivision.getDivision()));
//            if(CBOX.getSelectionModel().getSelectedIndex() == 0){
//                SBOX.setOpacity(100);
//                for(int i = 0; i < 51; i++){
//                    finalFLDVALUS.add(fldVAL.get(i));
//                }
//                SBOX.setItems(finalFLDVALUS);
//            }
//            if(CBOX.getSelectionModel().getSelectedIndex() == 1){
//                SBOX.setOpacity(100);
//                for(int i = 64; i < 68; i++){
//                    finalFLDVALUK.add(fldVAL.get(i));
//                }
//                SBOX.setItems(finalFLDVALUK);
//            }
//            if(CBOX.getSelectionModel().getSelectedIndex() == 2){
//                SBOX.setOpacity(100);
//                for(int i = 51; i < 64; i++){
//                    finalFLDVALCA.add(fldVAL.get(i));
//                }
//                SBOX.setItems(finalFLDVALCA);
////            }
//
//        });
//
//
//        // gathers info on division id
//        System.out.println(SBOX.getSelectionModel().getSelectedItem());
//        // query db based off division


    }


    /**
     * This method is used to populate the drop-down menus.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hiddenVar.setOpacity(0);
        try {
            ObservableList<FirstLevelDivisions> FLD = FirstLevelDivisionDAO.getFLD();
            ObservableList<String> fldVAL = FXCollections.observableArrayList();
            ObservableList<Integer> fldCC = FXCollections.observableArrayList();
            ObservableList<String> finalFLDVALUS = FXCollections.observableArrayList();
            ObservableList<String> finalFLDVALCA = FXCollections.observableArrayList();
            ObservableList<String> finalFLDVALUK = FXCollections.observableArrayList();

            CBOX.setOnAction( e ->{
                FLD.forEach((firstLevelDivisions -> fldCC.add(firstLevelDivisions.getCountryID())));
                FLD.forEach(firstLevelDivision -> fldVAL.add(firstLevelDivision.getDivision()));
                if(CBOX.getSelectionModel().getSelectedIndex() == 0){
                    SBOX.setOpacity(100);
                    for(int i = 0; i < 51; i++){
                        finalFLDVALUS.add(fldVAL.get(i));
                    }
                    SBOX.setItems(finalFLDVALUS);
                }
                if(CBOX.getSelectionModel().getSelectedIndex() == 1){
                    SBOX.setOpacity(100);
                    for(int i = 64; i < 68; i++){
                        finalFLDVALUK.add(fldVAL.get(i));
                    }
                    SBOX.setItems(finalFLDVALUK);
                }
                if(CBOX.getSelectionModel().getSelectedIndex() == 2){
                    SBOX.setOpacity(100);
                    for(int i = 51; i < 64; i++){
                        finalFLDVALCA.add(fldVAL.get(i));
                    }
                    SBOX.setItems(finalFLDVALCA);
                }

            });

            // lists
            ObservableList<Countries> country = CountryDAO.getCountries();
            ObservableList<String> countryVal = FXCollections.observableArrayList();

            //cycle each country add to country box
            country.stream().map(Countries::getCountry).forEach(countryVal::add);
            CBOX.setItems(countryVal);
            // Hides the state/prov box until a country selection is made.
            SBOX.setOpacity(0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
