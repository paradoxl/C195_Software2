package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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

    public void back(ActionEvent actionEvent) {
    }
}
