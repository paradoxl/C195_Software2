package com.michael.c195_software2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;


public class Log_in_controller {
    @FXML
    private Button exitBTN;
    @FXML
    private Button signInBTN;
    Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);

    /**
     * This method is used to confirm the user is authentic.
     * this method will also pass the user to a default customer-view form once the user is authenticated.
     * @param actionEvent
     * @throws IOException
     */
    public void signIn(ActionEvent actionEvent) throws IOException {
        // check user is legit


        // Move to the customer view

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Customer-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer View");
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent actionEvent) {
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
