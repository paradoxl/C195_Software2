package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.ContactDAO;
import com.michael.c195_software2.DataAccessObject.CustomerDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.ResourceBundle;

public class add_appointment_controller implements Initializable {

    @FXML
    public TextField appointmentIDTextFLD;
    @FXML
    public TextField titleTextFLD;
    @FXML
    public TextField descriptionTextFLD;
    @FXML
    public TextField locationTextFLD;
    @FXML
    public TextField typeTextFLD;
    @FXML
    public TextField userIDTextFLD;
    @FXML
    public ComboBox contactBOX;
    @FXML
    public DatePicker startTextFLD;
    @FXML
    public DatePicker endTextFLD;

    @FXML
    public TableView customerTABLE;
    @FXML
    public TableColumn idCELL;
    @FXML
    public TableColumn nameCELL;
    @FXML
    public TableColumn phoneCELL;
    @FXML
    public ComboBox startTimeBox;
    @FXML
    public ComboBox endTimeBOX;
    @FXML
    public ComboBox CustomerIDBOX;

    Alert exit = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit?", ButtonType.YES, ButtonType.NO);

    /**
     * This method is used to save user created appointments.
     * Generates a random appointment id
     * Pulls data from text-fields and combo box to populate tables.
     * @param actionEvent
     * @throws SQLException
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException {
        //This will pull in the appointment ids and generate a new one randomly. This will only be done if the random is not already listed.
        ObservableList<Integer> list = FXCollections.observableArrayList();
        Appointments newAppointment = new Appointments();
        String query = "SELECT Appointment_ID FROM appointments";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        int appID = 1;
        while (rs.next()){
            list.add(rs.getInt("Appointment_ID"));
            Random ran = new Random();
            while (list.contains(appID)){
                appID = ran.nextInt(10000);
            }
        }

        // populate combo box



    }

    public void exitButton(ActionEvent actionEvent) throws IOException {
        exit.showAndWait();
        if(exit.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        else {
            System.out.println("no touchy!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    //implement the choice box
        try {
            //Customer Table
            ObservableList<Customers> customers = CustomerDAO.getCustomers();
            customerTABLE.setItems(customers);
            idCELL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCELL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            phoneCELL.setCellValueFactory(new PropertyValueFactory<>("phone"));

            //contact Combobox
            ObservableList<Contacts> contacts = ContactDAO.getContacts();
            ObservableList<String> contactVALS = FXCollections.observableArrayList();
            contacts.stream().map(Contacts::getContactName).forEach(contactVALS::add);
            contactBOX.setItems(contactVALS);

            //Customer ID box
            ObservableList<Customers> customerID = CustomerDAO.getCustomers();
            ObservableList<Integer> customerIDVALS = FXCollections.observableArrayList();
            customerID.stream().map(Customers::getCustomerID).forEach(customerIDVALS::add);
            CustomerIDBOX.setItems(customerIDVALS);

            //Time Boxes
            LocalTime start = LocalTime.MIN.plusHours(8);
            LocalTime end = LocalTime.MIN.plusHours(23);

            ObservableList<LocalTime> timeIsntReal = FXCollections.observableArrayList();
            while(start.isBefore(end)){
                timeIsntReal.add(start);
                start = start.plusHours(1);
            }
            startTimeBox.setItems(timeIsntReal);
            endTimeBOX.setItems(timeIsntReal);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
