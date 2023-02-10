package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.ContactDAO;
import com.michael.c195_software2.dataBaseConnection.InitCon;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class will allow for the generation of a report based on Contacts.
 */
public class contactReportController implements Initializable {
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
    public ComboBox contactBOX;

    /**
     * This method is used to return the user back to the appointments view.
     */
    Alert exit = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to return to the previous page?", ButtonType.YES,ButtonType.NO);
    public void back(ActionEvent actionEvent) throws IOException {
        exit.showAndWait();
        if(exit.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        else {
            System.out.println("why even put the button there anymore.");
        }
    }

    /**
     * This method will generate a report based on the selected contact.
     * @param actionEvent
     * @throws SQLException
     */
    public void generateSchedule(ActionEvent actionEvent) throws SQLException {
        int contactID = 0;
        ObservableList<Appointments> appList = FXCollections.observableArrayList();
        ObservableList<Appointments>appointments = AppointmentDAO.getAppointment();

        //get correct id
        String query = "Select Contact_ID from contacts WHERE Contact_Name = '"+ contactBOX.getValue() +"'";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            contactID = rs.getInt("Contact_ID");
        }
        // pull all appointments with that contact id
        String appQuery = "Select * from appointments WHERE Contact_Id = '"+ contactID + "'";
        PreparedStatement appPS = InitCon.connection.prepareStatement(appQuery);
        ResultSet appRs = appPS.executeQuery();


        for(Appointments app : appointments){
            if(app.getContactID() == contactID) {
                appList.add(app);
            }
        }

        scheduleTable.setItems(appList);
        idCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCOL.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
        custIDCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        sTIMECOL.setCellValueFactory(new PropertyValueFactory<>("start"));
        eTIMECOL.setCellValueFactory(new PropertyValueFactory<>("end"));



    }

    /**
     * This method will implement the contact box with all contacts in the system.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //contact Combobox
        try {
            ObservableList<Contacts> contacts = ContactDAO.getContacts();
            ObservableList<String> contactVALS = FXCollections.observableArrayList();
            contacts.stream().map(Contacts::getContactName).forEach(contactVALS::add);
            contactBOX.setItems(contactVALS);
        }
        catch (SQLException e){
            Alert sqlE = new Alert(Alert.AlertType.ERROR,"There was an issue grabbing information from the database", ButtonType.OK);
            sqlE.showAndWait();
        }

    }
}
