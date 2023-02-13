package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CountryDAO;
import com.michael.c195_software2.DataAccessObject.FirstLevelDivisionDAO;
import com.michael.c195_software2.dataBaseConnection.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class is used to update the customer records.
 */
public class update_customer_view_controller implements Initializable {
    @FXML
    public Button save;
    @FXML
    public Button cancel;
    @FXML
    public TextField hiddenVar;
    @FXML
    public ComboBox CBOX;
    @FXML
    public ComboBox SBOX;
    @FXML
    private TextField nameTextFLD;
    @FXML
    private TextField addressTextFLD;
    @FXML
    private TextField postalTextFLD;
    @FXML
    private TextField phoneTextFLD;
    @FXML
    private TextField custIDTextFLD;


    Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to cancel?", ButtonType.YES, ButtonType.NO);
    Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to save?",ButtonType.YES, ButtonType.NO);

    private Customers selected;

    /**
     * This method is used to populate all fields with the customer's data.
     * This method contains lambda number two "country.stream().map(countries -> countries.getCountry()).forEach(countryVal::add);"
     * This lambda is used to add the country name to an observable list.
     * @param selected
     */
    public void populate(Customers selected){
        //Pull data in
        this.selected = selected;
        int id = selected.getCustomerID();
        String name = selected.getCustomerName();
        String address = selected.getAddress();
        String postal = selected.getPostalCode();
        String phone = selected.getPhone();
        int divisionID = selected.getDivisionID();

        //assign data to fields
        custIDTextFLD.setText(String.valueOf(id));
        nameTextFLD.setText(name);
        addressTextFLD.setText(address);
        postalTextFLD.setText(postal);
        phoneTextFLD.setText(phone);


        //we need to pull in the current selected values and then populate.
        hiddenVar.setOpacity(0);
        try {
            // lists
            ObservableList<Countries> country = CountryDAO.getCountries();
            ObservableList<String> countryVal = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivisions> fld = FirstLevelDivisionDAO.getFLD();
            ObservableList<String> fldVAl = FXCollections.observableArrayList();

            String choice = "";

            for(FirstLevelDivisions divs:fld){
                if(selected.getDivisionID() == divs.getDivisionID()){
                    SBOX.setValue(divs.getDivision());
                    //set country based on division.

                }
            }


                for(int i = 0; i < 51; i++){
                    if(selected.getDivisionID() == i) {
                        CBOX.setValue("U.S");
                    }
                }
                for(int i = 64; i < 104; i++){
                    if(selected.getDivisionID() == i) {
                        CBOX.setValue("UK");
                    }
                }
                for(int i = 51; i < 64; i++){
                    if(selected.getDivisionID() == i) {
                        CBOX.setValue("Canada");
                    }
                }

            //Lambda Number two
            country.stream().map(countries -> countries.getCountry()).forEach(countryVal::add);
            CBOX.setItems(countryVal);

            // Hides the state/prov box until a country selection is made.
//            SBOX.setOpacity(0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * This method will return the user to the customer view page.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This method will save customer records to the database.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public void save(ActionEvent actionEvent) throws SQLException, IOException, InterruptedException {

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




        //TODO: Collect which user is logged in so we can add the changed by.
        String insertQuery = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ? WHERE Customer_ID = '" + custIDTextFLD.getText() + "'";
        PreparedStatement insertPS = InitCon.connection.prepareStatement(insertQuery);

        insertPS.setString(1,nameTextFLD.getText());
        insertPS.setString(2,addressTextFLD.getText());
        insertPS.setString(3, postalTextFLD.getText());
        insertPS.setString(4,phoneTextFLD.getText());
//        insertPS.setString(7,"i need to fix this to collect user");
        insertPS.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
//        insertPS.setString(9,"I need to fix this");

        saveAlert.showAndWait();
        if(saveAlert.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxmimplements Initializablel"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
            insertPS.executeUpdate();
        }
        else {
            System.out.println("Yall have to stop hitting buttons if you dont mean to.");
        }

    }

    /**
     * This method is used to populate the country and state combo boxes.
     * This method contains a copy of the first lambda i used.
     * @param actionEvent
     * @throws SQLException
     */
    public void dynamicCombo(ActionEvent actionEvent) throws SQLException {
        // sets combo boxes
        SBOX.setOpacity(100);
        ObservableList<FirstLevelDivisions> FLD = FirstLevelDivisionDAO.getFLD();
        ObservableList<String> fldVAL = FXCollections.observableArrayList();
        ObservableList<Integer> fldCC = FXCollections.observableArrayList();
        ObservableList<String> finalFLDVALUS = FXCollections.observableArrayList();
        ObservableList<String> finalFLDVALCA = FXCollections.observableArrayList();
        ObservableList<String> finalFLDVALUK = FXCollections.observableArrayList();
        //First set of LAMBDA expressions used to gather data on FLD base on country id
        FLD.forEach((firstLevelDivisions -> fldCC.add(firstLevelDivisions.getCountryID()) ));
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

        // gathers info on division id
        System.out.println(SBOX.getSelectionModel().getSelectedItem());
        // query db based off division

    }

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
    }catch (SQLException e){
            Alert cancelAlert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            cancelAlert.showAndWait();
        }
        }
}
