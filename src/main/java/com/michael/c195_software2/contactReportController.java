package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.ContactDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
    public TableColumn sDATECOL;
    @FXML
    public TableColumn sTIMECOL;
    @FXML
    public TableColumn eDATECOL;
    @FXML
    public TableColumn eTIMECOL;
    @FXML
    public TableColumn custIDCOL;
    public ComboBox contactBOX;

    public void back(ActionEvent actionEvent) {

    }


    public void generateSchedule(ActionEvent actionEvent) throws SQLException {
        int contactID = 0;
        String queryContactID = "Select Contact_ID from appointments WHERE Contact_Name = '" + contactBOX.getValue() + "'";
        PreparedStatement ps = InitCon.connection.prepareStatement(queryContactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            contactID = rs.getInt("Contact_ID");
        }
        ObservableList<Appointments> appList = FXCollections.observableArrayList();
        ObservableList<Appointments>appointments = AppointmentDAO.getAppointment();
//        String appQuery = "SELECT * FROM appointments WHERE Contact_ID = '" + contactID + "'";
//        PreparedStatement psApp = InitCon.connection.prepareStatement(appQuery);
//        ResultSet rsApp = psApp.executeQuery();
//        while (rsApp.next()){
//            scheduleTable.setItems();
//        }

        for(Appointments app : appointments){
            if(app.getContactID() == contactID){
                appList.add(app);
            }
        }

        scheduleTable.setItems(appList);
        idCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCOL.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
        custIDCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));



    }

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
