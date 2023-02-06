package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.ContactDAO;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.ResourceBundle;

public class appointment_view_controller implements Initializable {
    public ComboBox reportBox;
    public Button generateBTN;
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

    /**
     * This method is used to populate all tables and values on the page.
     * @param url
     * @param resourceBundle
     */
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


            //Reports
            ObservableList<String> reports = FXCollections.observableArrayList();
            reports.add("Total Appointments");
            reports.add("Schedule by contact");
            reports.add("Schedule before lunch");
            reportBox.setItems(reports);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method returns the user to the previous page.
     * @param actionEvent
     * @throws IOException
     */
    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method takes the user to a form to add appointments.
     * @param actionEvent
     * @throws IOException
     */
    public void addAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add-appointment-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will delete appointments from the system.
     * @param actionEvent
     * @throws SQLException
     */
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

    /**
     * This method is used to change existing appointments.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
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

    /**
     * This method will sort the table by appointments on a weekly basis
     * @param actionEvent
     * @throws SQLException
     */
    public void sortWeekly(ActionEvent actionEvent) throws SQLException {
        LocalDateTime start = LocalDateTime.now().minusWeeks(1);
        LocalDateTime end = LocalDateTime.now().plusWeeks(1);

        ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
        ObservableList<Appointments> appointmentsVAL = FXCollections.observableArrayList();

        for(Appointments appointment: appointments){
            if (appointment.getStart().isAfter(start) && appointment.getStart().isBefore(end)){
                appointmentsVAL.add(appointment);
            }
            appointmentTABLE.setItems(appointmentsVAL);
        }


    }

    /**
     * This method will sort the table by appointments on a monthly basis
     * @param actionEvent
     * @throws SQLException
     */
    public void sortMonthly(ActionEvent actionEvent) throws SQLException {
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);

        ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
        ObservableList<Appointments> appointmentsVAL = FXCollections.observableArrayList();

        for(Appointments appointment: appointments){
            if (appointment.getStart().isAfter(start) && appointment.getStart().isBefore(end)){
                appointmentsVAL.add(appointment);
            }
            appointmentTABLE.setItems(appointmentsVAL);
        }
    }

    /**
     * This method will display all appointments.
     * @param actionEvent
     * @throws SQLException
     */
    public void sortAll(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointments> appointments = AppointmentDAO.getAppointment();
        if(appointments.isEmpty()){
            System.out.println("No Appointments");
        }
        else {
            appointmentTABLE.setItems(appointments);
        }

    }

    public void generateReport(ActionEvent actionEvent) throws IOException {
        if(reportBox.getValue() == "Total Appointments"){
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("totalAppointmentReport.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Reports");
            stage.setScene(scene);
            stage.show();
        }
        if(reportBox.getValue() == "Schedule by contact"){
            //generate report
        }
        if(reportBox.getValue() == "Schedule before lunch"){
            //generate report.
        }
    }
}
