package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CustomerDAO;
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
import java.util.ResourceBundle;

public class customer_view_controller implements Initializable {
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

    Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);

    /**
     * This method is used to populate the tableview found on customer view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customers> list = CustomerDAO.getCustomers();
            customerTable.setItems(list);
            idCOL.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCOL.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCOL.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCOL.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCOL.setCellValueFactory(new PropertyValueFactory<>("phone"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRecord(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add-customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("ADD RECORD");
        stage.setScene(scene);
        stage.show();
    }


    public void exit(ActionEvent actionEvent) {
        exitAlert.showAndWait();
        if(exitAlert.getResult() == ButtonType.YES){
            System.exit(0);
        }
        else{
            System.out.println("why did you touch that??");
        }
    }
}
