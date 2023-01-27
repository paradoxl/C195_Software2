package com.michael.c195_software2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class update_customer_view_controller {
    @FXML
    private TextField nameTextFLD;
    @FXML
    private TextField addressTextFLD;
    @FXML
    private TextField postalTextFLD;
    @FXML
    private TextField phoneTextFLD;


    private Customers selected;
    public void populate(Customers selected){
        //Pull data in
        this.selected = selected;
        int id = selected.getCustomerID();
        String name = selected.getCustomerName();
        String address = selected.getAddress();
        String postal = selected.getPostalCode();
        String phone = selected.getPhone();

        //assign data to fields
        nameTextFLD.setText(name);
        addressTextFLD.setText(address);
        postalTextFLD.setText(postal);
        phoneTextFLD.setText(phone);


    }
}
