package com.michael.c195_software2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
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


    /**
     * This method is used to populate the tableview found on customer view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
