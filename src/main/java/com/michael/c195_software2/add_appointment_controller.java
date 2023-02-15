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
import java.util.Random;
import java.util.ResourceBundle;

/**
 * This class is used to control the screen for adding appointments.
 */
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
    Alert zoned = new Alert(Alert.AlertType.ERROR,"You have chosen a time that is outside of our business hours. Note: our office is in Eastern Standard Time",ButtonType.OK );
    Alert overlap = new Alert(Alert.AlertType.ERROR, "This appointment overlaps with another for this customer. Please choose a different time");

    /**
     * This method is used to save user created appointments.
     * Generates a random appointment id
     * Pulls data from text-fields and combo box to populate tables.
     * Checks for overlapping appointments.
     * @param actionEvent
     * @throws SQLException
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException {

        Alert save = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to save this appointment?", ButtonType.YES,ButtonType.NO);
        save.showAndWait();

        if (save.getResult() == ButtonType.YES) {

            // Error checks for individual fields.
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
            if (startTextFLD.getValue() == null) {
                Alert noStart = new Alert(Alert.AlertType.ERROR, "You have not selected a start date", ButtonType.OK);
                noStart.showAndWait();
                return;
            }
            if (endTextFLD.getValue() == null) {
                Alert noEnd = new Alert(Alert.AlertType.ERROR, "You have not selected an end date", ButtonType.OK);
                noEnd.showAndWait();
                return;
            }
            if (endTimeBOX.getValue() == null) {
                Alert noEndTime = new Alert(Alert.AlertType.ERROR, "You have not selected an end time", ButtonType.OK);
                noEndTime.showAndWait();
                return;
            }
            if (startTimeBox.getValue() == null) {
                Alert noStartTime = new Alert(Alert.AlertType.ERROR, "You have not selected a start time", ButtonType.OK);
                noStartTime.showAndWait();
                return;
            }
            //end error checks


            //check that appointments do not overlap
//        ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
//        ObservableList<Appointments>   vals = FXCollections.observableArrayList();
//        LocalTime checkStart = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
//        LocalTime checkStartEnd = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
//
//        int overlapCheck = (int) CustomerIDBOX.getValue();
//        for(Appointments app: appointments){
//            LocalTime first = LocalTime.from(app.getStart());
//            LocalTime second = LocalTime.from(app.getEnd());
//            if(overlapCheck == app.getCustomerID()){
//                if(checkStart.isAfter(first) && checkStart.isBefore(second) || checkStartEnd.isAfter(first) && checkStartEnd.isBefore(second)){
//                    System.out.println("MADE IT HERE");
//                    overlap.showAndWait();
//                    return;
//                }
//            }
//        }


            //This will pull in the appointment ids and generate a new one randomly. This will only be done if the random is not already listed.
            ObservableList<Integer> list = FXCollections.observableArrayList();
            Appointments newAppointment = new Appointments();
            String query = "SELECT Appointment_ID FROM appointments";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int appID = 1;
            while (rs.next()) {
                list.add(rs.getInt("Appointment_ID"));
                Random ran = new Random();
                while (list.contains(appID)) {
                    appID = ran.nextInt(10000);
                }
            }
//            gather times to be saved to database.
            LocalTime startTime = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
            LocalTime endTime = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
            LocalDate startDate = startTextFLD.getValue();
            LocalDate endDate = endTextFLD.getValue();

            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

            ZonedDateTime startConv = start.atZone(ZoneId.systemDefault());
            ZonedDateTime utcStart = startConv.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endConv = end.atZone(ZoneId.systemDefault());
            ZonedDateTime utcEnd = endConv.withZoneSameInstant(ZoneOffset.UTC);


            LocalDateTime DTStartforSchedule = LocalDateTime.of(startDate, startTime);
            LocalDateTime DTEndforSchedule = LocalDateTime.of(endDate, endTime);

            ZonedDateTime zoneStartforSchedule = ZonedDateTime.of(DTStartforSchedule, ZoneId.systemDefault());
            ZonedDateTime zoneEndforSchedule = ZonedDateTime.of(DTEndforSchedule, ZoneId.systemDefault());

            ZonedDateTime convertStartESTforSchedule = zoneStartforSchedule.withZoneSameInstant(ZoneId.of("US/Eastern"));
            ZonedDateTime convertEndforSchedule = zoneEndforSchedule.withZoneSameInstant(ZoneId.of("US/Eastern"));


            // open and close in est
            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(22, 0);
            LocalDateTime DTStart = LocalDateTime.of(startDate, open);
            LocalDateTime DTEnd = LocalDateTime.of(endDate, close);

            ZonedDateTime zoneStart = ZonedDateTime.of(DTStart, ZoneId.systemDefault());
            ZonedDateTime zoneEnd = ZonedDateTime.of(DTEnd, ZoneId.systemDefault());

            ZonedDateTime convertStartEST = zoneStart.withZoneSameInstant(ZoneId.of("US/Eastern"));
            ZonedDateTime convertEndEST = zoneEnd.withZoneSameInstant(ZoneId.of("US/Eastern"));


            if (convertStartESTforSchedule.isBefore(convertStartEST) || convertStartEST.isAfter(convertEndEST) || convertEndforSchedule.isAfter(convertEndEST) || convertEndforSchedule.isBefore(convertStartEST)) {
                zoned.showAndWait();
                return;
            }


            if (utcStart.isAfter(utcEnd)) {
                Alert beforeStart = new Alert(Alert.AlertType.ERROR, "Start time is scheduled for after the end time", ButtonType.OK);
                beforeStart.showAndWait();
                return;
            }

            if (utcStart.equals(utcEnd)) {
                Alert same = new Alert(Alert.AlertType.ERROR, "Start time is the same time as the end.", ButtonType.OK);
                same.showAndWait();
                return;
            }

            //Check overlap.
            int customerID = (int) CustomerIDBOX.getSelectionModel().getSelectedItem();
            ObservableList<Appointments> appVAL = AppointmentDAO.getAppointment();
            for (Appointments appointment : appVAL) {


                LocalDateTime checkStart = appointment.getStart();
                LocalDateTime checkEnd = appointment.getEnd();
                System.out.println("Check start: " + checkStart);
                System.out.println("start: " + start);

                if ((customerID == appointment.getCustomerID()) && (appID != appointment.getAppointmentID()) && (start.isBefore(checkStart)) && (end.isAfter(checkEnd))) {
                    Alert overlap = new Alert(Alert.AlertType.ERROR, "Appointment overlaps with an existing appointment.", ButtonType.OK);
                    overlap.showAndWait();
                    System.out.println("Overlap");
                    return;
                }
                if ((customerID == appointment.getCustomerID()) && (appID != appointment.getAppointmentID()) && (start.isAfter(checkStart)) && (start.isBefore(checkEnd))) {
                    Alert sT = new Alert(Alert.AlertType.ERROR, "Start time overlaps with an existing appointment.", ButtonType.OK);
                    sT.showAndWait();
                    System.out.println("Overlap");
                    return;
                }
                // 10 - 11
                if (customerID == appointment.getCustomerID() && (appID != appointment.getAppointmentID()) && (end.isAfter(checkStart)) && (end.isBefore(checkEnd))) {
                    Alert endAlert = new Alert(Alert.AlertType.ERROR, "End time overlaps with an existing appointment.", ButtonType.OK);
                    endAlert.showAndWait();
                    System.out.println("Overlap");
                    return;
                }
                if (customerID == appointment.getCustomerID() && (appID != appointment.getAppointmentID()) && start.equals(checkStart)) {
                    Alert sameStart = new Alert(Alert.AlertType.ERROR, "Start time is the same as another appointment for this customer", ButtonType.OK);
                    sameStart.showAndWait();
                    return;
                }
                if (customerID == appointment.getCustomerID() && (appID != appointment.getAppointmentID()) && end.equals(checkEnd)) {
                    Alert sameEnd = new Alert(Alert.AlertType.ERROR, "End time conflicts with another appointment for this customer.", ButtonType.OK);
                    sameEnd.showAndWait();
                    return;
                }
            }

                //gather customer ID

                //gather Contact ID
                int contactID = 0;
                String contactQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contactBOX.getSelectionModel().getSelectedItem() + "'";
                PreparedStatement contactPS = InitCon.connection.prepareStatement(contactQuery);
                ResultSet contactRS = contactPS.executeQuery();
                while (contactRS.next()) {
                    contactID = contactRS.getInt("Contact_ID");
                }

                //insert times

                String insertQuery = "INSERT INTO appointments (Appointment_ID,Customer_ID,Contact_ID,User_ID,Location,Title,Description,Type,Create_Date,Created_By,Last_Update,Last_Updated_By,Start,End) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement insertPS = InitCon.connection.prepareStatement(insertQuery);
                insertPS.setInt(1, appID);
                insertPS.setInt(2, customerID);
                insertPS.setInt(3, contactID);
                insertPS.setInt(4, Integer.parseInt(userIDTextFLD.getText()));
                insertPS.setString(5, locationTextFLD.getText());
                insertPS.setString(6, titleTextFLD.getText());
                insertPS.setString(7, descriptionTextFLD.getText());
                insertPS.setString(8, typeTextFLD.getText());
                insertPS.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                insertPS.setString(10, "Help im trapped");
                insertPS.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
                insertPS.setString(12, "Call john connor!");
                insertPS.setTimestamp(13, Timestamp.valueOf(utcStart.toLocalDateTime()));
                insertPS.setTimestamp(14, Timestamp.valueOf(utcEnd.toLocalDateTime()));
                //TODO: customer id user id contact id
                //TODO: check that times do not conflict

                insertPS.executeUpdate();


                FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Appointments");
                stage.setScene(scene);
                stage.show();

        }
        else {
            System.out.println("y");
        }




    }

    /**
     * This method returns the user to the default appointments page.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButton(ActionEvent actionEvent) throws IOException {
        exit.showAndWait();
        if(exit.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
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

    /**
     * This method is used to initialize all populated fields.
     * @param url
     * @param resourceBundle
     */
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
            LocalTime  end = LocalTime .MIN.plusHours(22);


            ObservableList<LocalTime> timeIsntReal = FXCollections.observableArrayList();
            while(start.isBefore(end)){
                timeIsntReal.add(start);
                start = start.plusMinutes(15);
            }
            startTimeBox.setItems(timeIsntReal);
            endTimeBOX.setItems(timeIsntReal);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
