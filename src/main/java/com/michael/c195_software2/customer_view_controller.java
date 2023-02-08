package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CountryDAO;
import com.michael.c195_software2.DataAccessObject.CustomerDAO;
import com.michael.c195_software2.DataAccessObject.FirstLevelDivisionDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class customer_view_controller implements Initializable {
    @FXML
    public TableColumn countryCOL;
    @FXML
    public TableColumn stateCOL;
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn<?,?> idCOL;
    @FXML
    private TableColumn<?,?> nameCOL;
    @FXML
    private TableColumn<?,?> addressCOL;
    @FXML
    private TableColumn<?,?> postalCOL;
    @FXML
    private TableColumn<?,?> phoneCOL;

    private Parent scene;
    Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
    Alert noSelectedCust = new Alert(Alert.AlertType.ERROR, "You have not selected a customer", ButtonType.OK);
    Alert hasAppointments = new Alert(Alert.AlertType.ERROR, "You cannot remove a patient that has appointments", ButtonType.OK);
    /**
     * This method is used to populate the tableview found on customer view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customers> list = CustomerDAO.getCustomers();
            ObservableList<FirstLevelDivisions> div = FirstLevelDivisionDAO.getFLD();
            ObservableList<Countries> countries = CountryDAO.getCountries();
            ObservableList<String> vals = FXCollections.observableArrayList();



            customerTable.setItems(list);
            idCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCOL.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCOL.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));
            stateCOL.setCellValueFactory(new PropertyValueFactory<>("divisionID"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will take the user to a form to add new Customer Records
     * @param actionEvent
     * @throws IOException
     */
    public void addRecord(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add-customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("ADD RECORD");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will take the user to a form to update existing Records.
     * @param actionEvent
     * @throws IOException
     */
    public void updateRecord(ActionEvent actionEvent)throws IOException{
        Customers selected = customerTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            noSelectedCust.showAndWait();
            return;
        }
        try {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-customer-view.fxml"));
            scene = loader.load();
            update_customer_view_controller helper = loader.getController();
            helper.populate(selected);
            stage.setTitle("UPDATE RECORD");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Error " + e);
        }
    }

    /**
     * This method will take the user to a form to view add update and delete appointments.
     * @param actionEvent
     * @throws IOException
     */
    public void viewAppointments(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("appointments-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("APPOINTMENTS");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * This method provides logic for the exit button.
     * @param actionEvent
     */
    public void exit(ActionEvent actionEvent) {
        exitAlert.showAndWait();
        if(exitAlert.getResult() == ButtonType.YES){
            InitCon.closeConnection();
            System.exit(0);
        }
        else{
            System.out.println("why did you touch that??");
        }
    }

    /**
     * This method provides logic for deletion of records.
     * error checking has been implemented to prevent patients from being deleted if they have pending appointments.
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteRecord(ActionEvent actionEvent) throws SQLException {
        ObservableList<Integer> idlist = FXCollections.observableArrayList();
        try {
            int selected = customerTable.getSelectionModel().getSelectedItem().getCustomerID();

            Alert deleteRecord = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this Customer Record? Record Number: " + selected, ButtonType.YES, ButtonType.NO);

            deleteRecord.showAndWait();
            if (deleteRecord.getResult() == ButtonType.YES) {
                String checkAppointment = "Select Customer_ID FROM appointments";
                PreparedStatement checkPS = InitCon.connection.prepareStatement(checkAppointment);
                ResultSet checkRS = checkPS.executeQuery();
                System.out.println("Gathering appointment data");

                while (checkRS.next()) {
                    idlist.add(checkRS.getInt("Customer_ID"));
                }
                if (idlist.contains(selected)) {
                    hasAppointments.showAndWait();
                } else {
                    String query = "DELETE FROM customers WHERE Customer_ID = '" + selected + "'";
                    Statement statement = InitCon.connection.createStatement();
                    statement.executeUpdate(query);
                    System.out.println("Begone Demon!");
                    ObservableList<Customers> list = CustomerDAO.getCustomers();
                    customerTable.setItems(list);
                }
            }
        }catch (NullPointerException e){
            noSelectedCust.showAndWait();
        }

//        if (customerTable.getSelectionModel().getSelectedItem() == null) {
//
//            return;
//        }


    }



    /**
     * This method is used to refresh the Table.
     * Its possible this will be deprecated.
     * @param actionEvent
     * @throws IOException
     */
    public void refresh(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Records");
        stage.setScene(scene);
        stage.show();
    }
}
