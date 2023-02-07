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
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException {
            //check that appointments do not overlap







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
            //gather times
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

            //check

            LocalDateTime open = LocalDateTime.MIN.plusHours(8);
            LocalDateTime close = LocalDateTime.MIN.plusHours(22);

            ZonedDateTime openConv = open.atZone(ZoneId.of("US/Eastern"));
            ZonedDateTime closeConv = close.atZone(ZoneId.of("US/Eastern"));
        System.out.println(open);
        System.out.println(openConv);

            //gather customer ID
            int customerID = (int) CustomerIDBOX.getSelectionModel().getSelectedItem();

            //gather Contact ID
            int contactID = 0;
            String contactQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contactBOX.getSelectionModel().getSelectedItem() + "'";
            PreparedStatement contactPS = InitCon.connection.prepareStatement(contactQuery);
            ResultSet contactRS = contactPS.executeQuery();
            while (contactRS.next()){
                contactID = contactRS.getInt("Contact_ID");
            }
        System.out.println(utcStart);
            //insert times
            String insertQuery = "INSERT INTO appointments (Appointment_ID,Customer_ID,User_ID,Contact_ID,Location,Title,Description,Type,Create_Date,Created_By,Last_Update,Last_Updated_By,Start,End) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

    /**
     * This method returns the user to the default appointments page.
     * @param actionEvent
     * @throws IOException
     */
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

            ZoneId eastern = ZoneId.of("US/Eastern");
            int difference = 0;
            
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
