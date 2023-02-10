package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class will show all appointments scheduled before noon on the day you are viewing it.
 */
public class MorningScheduleController implements Initializable {
    @FXML
    public TableView scheduleTable;
    @FXML
    public TableColumn idCOL;
    @FXML
    public TableColumn titleCOL;
    @FXML
    public TableColumn typeCOL;
    @FXML
    public TableColumn descriptionCOL;
    @FXML
    public TableColumn sTIMECOL;
    @FXML
    public TableColumn eTIMECOL;
    @FXML
    public TableColumn custIDCOL;

    /**
     * This method will populate the table with all appointments that are scheduled before noon.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
            ObservableList<Appointments> val = FXCollections.observableArrayList();

            LocalDateTime noon = LocalDateTime.now().toLocalDate().atTime(12,0);
            for(Appointments app : appointments){
                if(app.getStart().isBefore(noon)) {
                    val.add(app);
                }
            }

            scheduleTable.setItems(val);
            idCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCOL.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
            custIDCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            sTIMECOL.setCellValueFactory(new PropertyValueFactory<>("start"));
            eTIMECOL.setCellValueFactory(new PropertyValueFactory<>("end"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    /**
     * This method will take the user back to the appointments-view
     * @param actionEvent
     * @throws IOException
     */
    public void back(ActionEvent actionEvent) throws IOException {
        Alert back = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to go back? ", ButtonType.YES,ButtonType.NO);
        back.showAndWait();

        if (back.getResult() == ButtonType.YES) {

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Appointments");
            stage.setScene(scene);
            stage.show();
        }
    }
}
