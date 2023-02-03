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
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

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
    public DatePicker startTextFLD;
    @FXML
    public DatePicker endTextFLD;
    @FXML
    public ComboBox startTimeBox;
    @FXML
    public ComboBox endTimeBOX;
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
    private Appointments working;
    /**
     * This method is used to save new data to the database.
     * @param actionEvent
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException {
        int contactID = 0;
        String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?,End=?,Last_Update = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = '" + Integer.parseInt(appointmentIDTextFLD.getText()) + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);

        ps.setString(1, titleTextFLD.getText());
        ps.setString(2, descriptionTextFLD.getText());
        ps.setString(3, locationTextFLD.getText());
        String gatherContactID = "SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contactBOX.getSelectionModel().getSelectedItem() + "'";
        PreparedStatement gatherPS = InitCon.connection.prepareStatement(gatherContactID);
        ResultSet gatherRS = gatherPS.executeQuery();
        while (gatherRS.next()){
            contactID = gatherRS.getInt("Contact_ID");
        }
        ps.setInt(4,contactID);
        ps.setString(5, typeTextFLD.getText());

        LocalTime startTime = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
        LocalTime endTime = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
        LocalDate startDate = (LocalDate) startTextFLD.getValue();
        LocalDate endDate = (LocalDate) endTextFLD.getValue();

        LocalDateTime start = LocalDateTime.of(startDate,startTime);
        LocalDateTime end = LocalDateTime.of(endDate,endTime);

        ps.setTimestamp(6,Timestamp.valueOf(start));
        ps.setTimestamp(7,Timestamp.valueOf(end));
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(9, (Integer) CustomerIDBOX.getSelectionModel().getSelectedItem());
        ps.setInt(10, Integer.parseInt(userIDTextFLD.getText()));
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
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
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

        //refactoring times
        LocalDateTime start = working.getStart();
        LocalDateTime end = working.getEnd();

        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalDate endDate = end.toLocalDate();
        LocalTime endTime = end.toLocalTime();

        startTimeBox.setValue(startTime);
        endTimeBOX.setValue(endTime);
        startTextFLD.setValue(startDate);
        endTextFLD.setValue(endDate);

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
