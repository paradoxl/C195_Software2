package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.ContactDAO;
import com.michael.c195_software2.DataAccessObject.CustomerDAO;
import com.michael.c195_software2.dataBaseConnection.InitCon;
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
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * This class is used to update appointments.
 */
public class update_appointment_controller implements Initializable{
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
    public TextField userIDTextFLD;
    @FXML
    public ComboBox contactBOX;
    @FXML
    public ComboBox CustomerIDBOX;
    @FXML
    public TableView customerTABLE;
    @FXML
    public TableColumn idCELL;
    @FXML
    public TableColumn nameCELL;
    @FXML
    public TableColumn phoneCELL;
    Alert exit = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit?",ButtonType.YES,ButtonType.NO);
    Alert overlap = new Alert(Alert.AlertType.ERROR, "This appointment overlaps with another for this customer. Please choose a different time");
    Alert startError = new Alert(Alert.AlertType.ERROR,"You have chosen a start/end time that is outside of our business hours. Note: our office is in Eastern Standard Time",ButtonType.OK );
    private Appointments working;
    /**
     * This method is used to save new data to the database.
     * @param actionEvent
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException {
        if (titleTextFLD.getText().isEmpty()) {
            Alert noTitle = new Alert(Alert.AlertType.ERROR, "You have not selected a title", ButtonType.OK);
            noTitle.showAndWait();
            return;
        }
        if (descriptionTextFLD.getText().isEmpty()) {
            Alert noDescription = new Alert(Alert.AlertType.ERROR, "You have not added a description", ButtonType.OK);
            noDescription.showAndWait();
            return;
        }
        if (locationTextFLD.getText().isEmpty()) {
            Alert noLocation = new Alert(Alert.AlertType.ERROR, "You have not added a location", ButtonType.OK);
            noLocation.showAndWait();
            return;
        }
        if (typeTextFLD.getText().isEmpty()) {
            Alert noType = new Alert(Alert.AlertType.ERROR, "You have not added a type", ButtonType.OK);
            noType.showAndWait();
            return;
        }
        if (userIDTextFLD.getText().isEmpty()) {
            Alert noUser = new Alert(Alert.AlertType.ERROR, "You have not added a user", ButtonType.OK);
            noUser.showAndWait();
            return;
        }
        if (contactBOX.getSelectionModel().getSelectedItem() == null) {
            Alert noContact = new Alert(Alert.AlertType.ERROR, "You have not added a contact", ButtonType.OK);
            noContact.showAndWait();
            return;
        }

        int contactID = 0;
        String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?,Last_Update = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = '" + Integer.parseInt(appointmentIDTextFLD.getText()) + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);

        ps.setString(1, titleTextFLD.getText());
        ps.setString(2, descriptionTextFLD.getText());
        ps.setString(3, locationTextFLD.getText());
        String gatherContactID = "SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contactBOX.getSelectionModel().getSelectedItem() + "'";
        PreparedStatement gatherPS = InitCon.connection.prepareStatement(gatherContactID);
        ResultSet gatherRS = gatherPS.executeQuery();
        while (gatherRS.next()) {
            contactID = gatherRS.getInt("Contact_ID");
        }
        ps.setInt(4, contactID);
        ps.setString(5, typeTextFLD.getText());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, (Integer) CustomerIDBOX.getSelectionModel().getSelectedItem());
        ps.setInt(8, Integer.parseInt(userIDTextFLD.getText()));
        ps.executeUpdate();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();

    }


    /**
     * This method is used to return the user to the main appointments page.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButton(ActionEvent actionEvent) throws IOException {
        exit.showAndWait();

        if (exit.getResult() == ButtonType.YES){
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        else {
            System.out.println("push the right buttons please.");
        }

    }

    /**
     * This method is used to populate all fields found on the page with the selected appointment.
     * @param working
     * @throws SQLException
     */
    public void populate(Appointments working) throws SQLException {
        //textBoxes
        this.working = working;
        int appointmentId = working.getAppointmentID();
        appointmentIDTextFLD.setText(String.valueOf(appointmentId));
        String title = working.getTitle();
        titleTextFLD.setText(title);
        String description = working.getDescription();
        descriptionTextFLD.setText(description);
        String location = working.getLocation();
        locationTextFLD.setText(location);
        String type = working.getType();
        typeTextFLD.setText(type);
        int customerID = working.getCustomerID();
        CustomerIDBOX.setValue(customerID);
        int userID = working.getUserID();
        userIDTextFLD.setText(String.valueOf(userID));


        // pulling data on contact id
        int contactID = working.getContactID();
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = '" + contactID + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        contactBOX.setValue(rs.getString("Contact_Name"));

        //set custome box right side
        ObservableList<Customers> customers = CustomerDAO.getCustomers();
        customerTABLE.setItems(customers);
        idCELL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCELL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneCELL.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    /**
     * This method is used to populate the fields that were not brought in through the populate method.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //set up contacts
            ObservableList<Contacts>  contacts = ContactDAO.getContacts();
            ObservableList<String> contactVALS = FXCollections.observableArrayList();
            contacts.stream().map(Contacts::getContactName).forEach(contactVALS::add);
            contactBOX.setItems(contactVALS);

            //set up customer ID
            ObservableList<Customers> customerID = CustomerDAO.getCustomers();
            ObservableList<Integer> customerIDVALS = FXCollections.observableArrayList();
            customerID.stream().map(Customers::getCustomerID).forEach(customerIDVALS::add);
            CustomerIDBOX.setItems(customerIDVALS);


            //set customer box right side
            ObservableList<Customers> customers = CustomerDAO.getCustomers();
            customerTABLE.setItems(customers);
            idCELL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCELL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            phoneCELL.setCellValueFactory(new PropertyValueFactory<>("phone"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
