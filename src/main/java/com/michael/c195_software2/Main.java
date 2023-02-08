package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.UserDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This is the driver class for the program.
 */
public class Main extends Application {
    /**
     * Start method
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        InitCon.openConnection();
        AppointmentDAO test  = new AppointmentDAO();
//        InitCon.closeConnection();
//        test.getAppointment();
        launch();
    }
}