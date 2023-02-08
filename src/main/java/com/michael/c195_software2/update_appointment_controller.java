package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
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
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
    Alert overlap = new Alert(Alert.AlertType.ERROR, "This appointment overlaps with another for this customer. Please choose a different time");
    Alert startError = new Alert(Alert.AlertType.ERROR,"You have chosen a start/end time that is outside of our business hours. Note: our office is in Eastern Standard Time",ButtonType.OK );
    private Appointments working;
    /**
     * This method is used to save new data to the database.
     * @param actionEvent
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException {
        if(titleTextFLD.getText().isEmpty()){
            Alert noTitle = new Alert(Alert.AlertType.ERROR,"You have not selected a title",ButtonType.OK);
            noTitle.showAndWait();
            return;
        }
        if (descriptionTextFLD.getText().isEmpty()){
            Alert noDescription = new Alert(Alert.AlertType.ERROR,"You have not added a description",ButtonType.OK);
            noDescription.showAndWait();
            return;
        }
        if (locationTextFLD.getText().isEmpty()){
            Alert noLocation = new Alert(Alert.AlertType.ERROR,"You have not added a location",ButtonType.OK);
            noLocation.showAndWait();
            return;
        }
        if (typeTextFLD.getText().isEmpty()){
            Alert noType = new Alert(Alert.AlertType.ERROR,"You have not added a type",ButtonType.OK);
            noType.showAndWait();
            return;
        }
        if(userIDTextFLD.getText().isEmpty()){
            Alert noUser = new Alert(Alert.AlertType.ERROR,"You have not added a user",ButtonType.OK);
            noUser.showAndWait();
            return;
        }
        if (contactBOX.getSelectionModel().getSelectedItem() == null){
            Alert noContact = new Alert(Alert.AlertType.ERROR,"You have not added a contact", ButtonType.OK);
            noContact.showAndWait();
            return;
        }
        if(startTextFLD.getValue() == null){
            Alert noStart = new Alert(Alert.AlertType.ERROR, "You have not selected a start date",ButtonType.OK);
            noStart.showAndWait();
            return;
        }
        if(endTextFLD.getValue() == null){
            Alert noEnd = new Alert(Alert.AlertType.ERROR, "You have not selected an end date",ButtonType.OK);
            noEnd.showAndWait();
            return;
        }
        if(endTimeBOX.getValue() == null){
            Alert noEndTime = new Alert(Alert.AlertType.ERROR, "You have not selected an end time",ButtonType.OK);
            noEndTime.showAndWait();
            return;
        }
        if(startTimeBox.getValue() == null){
            Alert noStartTime = new Alert(Alert.AlertType.ERROR,"You have not selected a start time",ButtonType.OK);
            noStartTime.showAndWait();
            return;
        }



        //check that appointments do not overlap
        ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
        ObservableList<Appointments>   vals = FXCollections.observableArrayList();
        LocalTime checkStart = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
        LocalDate startDateCheck =  startTextFLD.getValue();
        LocalDate endDateCheck = endTextFLD.getValue();
        LocalTime checkStartEnd = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
        LocalDate today = LocalDate.now();
        System.out.println(today);

        int overlapCheck = (int) CustomerIDBOX.getValue();
        for(Appointments app: appointments){
            LocalTime first = LocalTime.from(app.getStart());
            LocalTime second = LocalTime.from(app.getEnd());
            if(overlapCheck == app.getCustomerID()){
                if(checkStart.isAfter(first) && checkStart.isBefore(second)&& startDateCheck.isEqual(today) && endDateCheck.isEqual(today) || checkStartEnd.isAfter(first) && checkStartEnd.isBefore(second) && startDateCheck.isEqual(today) && endDateCheck.isEqual(today)){
                    if((app.getAppointmentID() != Integer.parseInt(appointmentIDTextFLD.getText()))) {
                        System.out.println("Start date " + startDateCheck);
                        System.out.println("today " + today);
                        System.out.println("MADE IT HERE");
                        overlap.showAndWait();
                        return;
                    }
                }
            }
        }


//        {
//            LocalDateTime checkStart = appointment.getStart();
//            LocalDateTime checkEnd = appointment.getEnd();

//            if ((customerID == appointment.getCustomerID()) && (newAppointmentID != appointment.getAppointmentID()) &&
//                    (dateTimeStart.isBefore(checkStart)) && (dateTimeEnd.isAfter(checkEnd))) {
//                return;
//            }
//
//            if ((customerID == appointment.getCustomerID()) && (newAppointmentID != appointment.getAppointmentID()) &&
//                    (dateTimeStart.isAfter(checkStart)) && (dateTimeStart.isBefore(checkEnd))) {
//                return;
//            }



        LocalTime startTimetest = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
        LocalTime endTimetest = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
        LocalDate startDatetest = startTextFLD.getValue();
        LocalDate endDatetest= endTextFLD.getValue();

        // converting scheduled time to est
        LocalDateTime DTStartforSchedule = LocalDateTime.of(startDatetest, startTimetest);
        LocalDateTime DTEndforSchedule = LocalDateTime.of(endDatetest, endTimetest);

        ZonedDateTime zoneStartforSchedule = ZonedDateTime.of(DTStartforSchedule, ZoneId.systemDefault());
        ZonedDateTime zoneEndforSchedule = ZonedDateTime.of(DTEndforSchedule, ZoneId.systemDefault());

        ZonedDateTime convertStartESTforSchedule = zoneStartforSchedule.withZoneSameInstant(ZoneId.of("US/Eastern"));
        ZonedDateTime convertEndforSchedule = zoneEndforSchedule.withZoneSameInstant(ZoneId.of("US/Eastern"));

        System.out.println("Schedule start EST: " + convertStartESTforSchedule);
        System.out.println("Schedule start local: " + startTimetest);



        // open and close in est
        LocalTime open = LocalTime.of(8,0);
        LocalTime close = LocalTime.of(22,0);
        LocalDateTime DTStart = LocalDateTime.of(startDatetest, open);
        LocalDateTime DTEnd = LocalDateTime.of(endDatetest, close);

        ZonedDateTime zoneStart = ZonedDateTime.of(DTStart, ZoneId.systemDefault());
        ZonedDateTime zoneEnd = ZonedDateTime.of(DTEnd, ZoneId.systemDefault());

        ZonedDateTime convertStartEST = zoneStart.withZoneSameInstant(ZoneId.of("US/Eastern"));
        ZonedDateTime convertEndEST = zoneEnd.withZoneSameInstant(ZoneId.of("US/Eastern"));

        System.out.println("open Time in Local: " +startTimetest);
        System.out.println("open Time in EST: " + convertStartEST);

        if(convertStartESTforSchedule.isBefore(convertStartEST) || convertStartEST.isAfter(convertEndEST)||convertEndforSchedule.isAfter(convertEndEST) || convertEndforSchedule.isBefore(convertStartEST)){
            startError.showAndWait();
            return;
        }



        //Check that the appointment times are before they end.
      LocalTime startCheck = (LocalTime) startTimeBox.getValue();
      LocalTime endCheck = (LocalTime) endTimeBOX.getValue();

      if (!startCheck.isBefore(endCheck)){
          System.out.println("Are you a time traveler?");
          Alert timeTravel = new Alert(Alert.AlertType.ERROR, "You have scheduled the appointment to start after it has already ended.",ButtonType.OK);
          timeTravel.showAndWait();
      }
      else {

          // Check that app is m-f
          HashSet<DayOfWeek> dateCheck = new HashSet<>();
          dateCheck.add(DayOfWeek.SATURDAY);
          dateCheck.add(DayOfWeek.SUNDAY);
          LocalDate startcheck = startTextFLD.getValue();
          LocalDate endCheckDay = endTextFLD.getValue();
          if (dateCheck.contains(startcheck.getDayOfWeek())) {
              Alert businessClosed = new Alert(Alert.AlertType.ERROR, "I'm sorry we are closed on weekends. Please schedule an appointment for a different day.", ButtonType.OK);
              businessClosed.showAndWait();

          } else if (dateCheck.contains(endCheckDay.getDayOfWeek())) {

              Alert bCEnd = new Alert(Alert.AlertType.ERROR, "I'm sorry we are closed on weekends, and will not be able to finish your appointment on this day. Please select a different end date.",ButtonType.OK);
              bCEnd.showAndWait();
          }
            else {

              int contactID = 0;
              String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?,End=?,Last_Update = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = '" + Integer.parseInt(appointmentIDTextFLD.getText()) + "'";
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

              LocalTime startTime = (LocalTime) startTimeBox.getSelectionModel().getSelectedItem();
              LocalTime endTime = (LocalTime) endTimeBOX.getSelectionModel().getSelectedItem();
              LocalDate startDate = (LocalDate) startTextFLD.getValue();
              LocalDate endDate = (LocalDate) endTextFLD.getValue();

              //Local time
              LocalDateTime start = LocalDateTime.of(startDate, startTime);
              LocalDateTime end = LocalDateTime.of(endDate, endTime);

              //converted to UTC
              ZonedDateTime startConv = start.atZone(ZoneId.systemDefault());
              ZonedDateTime utcStart = startConv.withZoneSameInstant(ZoneOffset.UTC);
              ZonedDateTime endConv = end.atZone(ZoneId.systemDefault());
              ZonedDateTime utcEnd = endConv.withZoneSameInstant(ZoneOffset.UTC);


              ps.setTimestamp(6, Timestamp.valueOf(utcStart.toLocalDateTime()));
              ps.setTimestamp(7, Timestamp.valueOf(utcEnd.toLocalDateTime()));
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
      }
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

        //refactoring times (pulls in time)
        LocalDateTime start = working.getStart();
        LocalDateTime end = working.getEnd();


        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalDate endDate = end.toLocalDate();
        LocalTime endTime = end.toLocalTime();


        //Create UTC NOW
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime conv = now.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = conv.withZoneSameInstant(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int difference = utc.getHour() - now.getHour();

//        // SUBTRACT DIFFERENCE
//        startTime = startTime.minusHours(difference);
//        endTime = endTime.minusHours(difference);


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
            LocalTime end = LocalTime.MIN.plusHours(22);

            ObservableList<LocalTime> timeIsntReal = FXCollections.observableArrayList();
            while(start.isBefore(end)){
                timeIsntReal.add(start);
                start = start.plusMinutes(15);
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
