package com.michael.c195_software2;

import com.michael.c195_software2.dataBaseConnection.InitCon;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class updateAppointmentTimeController implements Initializable {
    @FXML
    public Label Dynamic;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    public ComboBox startTime;
    public ComboBox endTime;

    private Appointments current;

    public void populate(Appointments current) throws SQLException{
        this.current = current;
        LocalDateTime start = current.getStart();
        LocalDateTime end = current.getEnd();


        LocalDate startDateVal = start.toLocalDate();
        LocalTime startTimeVal = start.toLocalTime();
        LocalDate endDateVal = end.toLocalDate();
        LocalTime endTimeVal = end.toLocalTime();


        //Create UTC NOW
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime conv = now.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = conv.withZoneSameInstant(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int difference = utc.getHour() - now.getHour();


        startTime.setValue(startTimeVal);
        endTime.setValue(endTimeVal);
        startDate.setValue(startDateVal);
        endDate.setValue(endDateVal);


    }

    public void save(ActionEvent actionEvent) throws SQLException, IOException {
        Alert save = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to save these times?", ButtonType.YES,ButtonType.NO);

        save.showAndWait();


        if (save.getResult() == ButtonType.YES) {
            LocalTime startTimeVal = (LocalTime) startTime.getSelectionModel().getSelectedItem();
            LocalTime endTimeVal = (LocalTime) endTime.getSelectionModel().getSelectedItem();
            LocalDate startDateVal = (LocalDate) startDate.getValue();
            LocalDate endDateVal = (LocalDate) endDate.getValue();

            //Local time
            LocalDateTime start = LocalDateTime.of(startDateVal, startTimeVal);
            LocalDateTime end = LocalDateTime.of(endDateVal, endTimeVal);

            //converted to UTC
            ZonedDateTime startConv = start.atZone(ZoneId.systemDefault());
            ZonedDateTime utcStart = startConv.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endConv = end.atZone(ZoneId.systemDefault());
            ZonedDateTime utcEnd = endConv.withZoneSameInstant(ZoneOffset.UTC);

            String query = "Update appointments SET  Start = ?, End = ? WHERE Appointment_ID = '" + current.getAppointmentID() +"'";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(utcStart.toLocalDateTime()));
            ps.setTimestamp(2, Timestamp.valueOf(utcEnd.toLocalDateTime()));
            ps.executeUpdate();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Appointments");
            stage.setScene(scene);
            stage.show();
        }
        else {
            System.out.println("Y then");
        }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalTime start = LocalTime.MIN.plusHours(8);
        LocalTime  end = LocalTime .MIN.plusHours(22);


        ObservableList<LocalTime> timeIsntReal = FXCollections.observableArrayList();
        while(start.isBefore(end)){
            timeIsntReal.add(start);
            start = start.plusMinutes(15);
        }
        startTime.setItems(timeIsntReal);
        endTime.setItems(timeIsntReal);
    }
}
