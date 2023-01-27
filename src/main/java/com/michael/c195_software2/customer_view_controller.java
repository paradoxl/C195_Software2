package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
