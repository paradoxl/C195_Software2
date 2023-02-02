package com.michael.c195_software2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class update_appointment_controller implements Initializable {
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
    public void saveButton(ActionEvent actionEvent) {
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
     * This method is used to populate all information on the page.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Textfield population
//        appointmentIDTextFLD.setText();


    }

    public void populate(Appointments working) {
        this.working = working;
        int appointmentId = working.getAppointmentID();
        appointmentIDTextFLD.setText(String.valueOf(appointmentId));
        String title = working.getTitle();
        String description = working.getDescription();
        String location = working.getLocation();
        String type = working.getType();
        LocalDateTime start = working.getStart();
        LocalDateTime end = working.getEnd();
        LocalDateTime createDate = working.getCreateDate();
        String createdBy = working.getCreatedBy();
        int customerID = working.getCustomerID();
        int userID = working.getUserID();
        int contactID = working.getContactID();
    }
}
