package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Formattable;
import java.util.ResourceBundle;

public class totalReportController implements Initializable {
    @FXML
    public Label dynamicLabel;
    @FXML
    public ComboBox monthBOX;
    @FXML
    public ComboBox typeBOX;
    @FXML
    public Button backBTN;

    public void genrateReport(ActionEvent actionEvent) throws SQLException {
        int counter = 0;

        String dropView = "DROP VIEW IF EXISTS view";
        Statement drop = InitCon.connection.createStatement();
        drop.executeUpdate(dropView);

        String createView = "CREATE VIEW view AS SELECT Type, MONTHNAME(Start) AS value FROM appointments";
        Statement statement =  InitCon.connection.createStatement();
        statement.executeUpdate(createView);

        String query = "SELECT COUNT(*) FROM view WHERE value = '" + monthBOX.getValue() + "' AND Type = '" + typeBOX.getValue() + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            counter = rs.getInt("count(*)");
        }
        dynamicLabel.setText(String.valueOf(counter));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
            ObservableList<String> appointmentsType = FXCollections.observableArrayList();
            ObservableList<String> distinct = FXCollections.observableArrayList();
            ObservableList<LocalDateTime> appointmentsDate = FXCollections.observableArrayList();
            ObservableList<Month> months = FXCollections.observableArrayList();


            appointments.stream().map(Appointments::getType).forEach(appointmentsType::add);
            appointments.stream().map(Appointments::getStart).forEach(appointmentsDate::add);


            for(String app: appointmentsType){
                if(!distinct.contains(app)){
                    distinct.add(app);
                }
            }
            // The word month has lost all meaning to me. Name your variables carefully kids.
            for(LocalDateTime month: appointmentsDate){
               if(!months.contains(month.getMonth())){
                   months.add(month.getMonth());
                }
            }



            typeBOX.setItems(distinct);
            monthBOX.setItems(months);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Alert back = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to leave this page?", ButtonType.YES,ButtonType.NO);
        back.showAndWait();
        if(back.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        else{
            System.out.println("stahhhp");
        }
    }
}
