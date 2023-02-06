package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.con.InitCon;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class appointment_view_controller implements Initializable {
    @FXML
    private TableView<Appointments> appointmentTABLE;
    @FXML
    private TableColumn<?,?> IDCOL;
    @FXML
    private TableColumn<?,?> titleCOL;
    @FXML
    private TableColumn<?,?> descriptionCOL;
    @FXML
    private TableColumn<?,?> locationCOL;
    @FXML
    private TableColumn<?,?>contactCOL;
    @FXML
    private TableColumn<?,?>typeCOL;
    @FXML
    private TableColumn<?,?>startCOL;
    @FXML
    private TableColumn<?,?>endCOL;
    @FXML
    private TableColumn<?,?>custIDCOL;
    @FXML
    private TableColumn<?,?>usrIDCOL;
    Alert noSelectedApp= new Alert(Alert.AlertType.ERROR, "You have not selected an appointment", ButtonType.OK);
    Alert delete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this appointment?",ButtonType.YES,ButtonType.NO);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointments> list = AppointmentDAO.getAppointment();
            appointmentTABLE.setItems(list);
            IDCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCOL.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCOL.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactCOL.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCOL.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCOL.setCellValueFactory(new PropertyValueFactory<>("end"));
            custIDCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            usrIDCOL.setCellValueFactory(new PropertyValueFactory<>("userID"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    public void addAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add-appointment-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAppointment(ActionEvent actionEvent) throws SQLException {
        if (appointmentTABLE.getSelectionModel().getSelectedItem() == null){
            noSelectedApp.showAndWait();
        }
        else {
            delete.showAndWait();
            if (delete.getResult() == ButtonType.YES) {
                int selected = appointmentTABLE.getSelectionModel().getSelectedItem().getAppointmentID();
                String query = "DELETE FROM appointments WHERE Appointment_ID = '" + selected + "'";
                Statement statement = InitCon.connection.createStatement();
                statement.executeUpdate(query);
                ObservableList<Appointments> list = AppointmentDAO.getAppointment();
                appointmentTABLE.setItems(list);
            }
            else{
                System.out.println("again with pushing buttons for no reason...");
            }
        }
    }

    public void updateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        Appointments selected = appointmentTABLE.getSelectionModel().getSelectedItem();
        if(selected == null){
            noSelectedApp.showAndWait();
        }
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("update-appointment-view.fxml"));
        Scene scene = new Scene(loader.load());
        update_appointment_controller helper = loader.getController();
        helper.populate(selected);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }
}
