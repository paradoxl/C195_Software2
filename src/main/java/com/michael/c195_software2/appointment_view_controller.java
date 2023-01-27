package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointments> list = AppointmentDAO.getAppointment();
            appointmentTABLE.setItems(list);
            IDCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
//            titleCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
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
}
