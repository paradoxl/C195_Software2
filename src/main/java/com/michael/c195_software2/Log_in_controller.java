package com.michael.c195_software2;

import com.michael.c195_software2.DataAccessObject.AppointmentDAO;
import com.michael.c195_software2.DataAccessObject.UserDAO;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


public class Log_in_controller implements Initializable{
    @FXML
    private Button exitBTN;
    @FXML
    private Button signInBTN;

    @FXML
    private TextField UsernameTextFLD;
    @FXML
    private TextField PasswordTextFLD;
    @FXML
    private Label country;
    Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);


    public void initialize(URL url, ResourceBundle resourceBundle) {

        //language settings
        Locale locale = Locale.getDefault();
        ZoneId zoneid = ZoneId.systemDefault();

        //elements on page that need to be dynamic
        resourceBundle = ResourceBundle.getBundle("lang",Locale.getDefault());
        signInBTN.setText(resourceBundle.getString(resourceBundle.getString("signin")));
        UsernameTextFLD.setPromptText(resourceBundle.getString("username"));
        PasswordTextFLD.setPromptText(resourceBundle.getString("password"));
        exitBTN.setText(resourceBundle.getString("exit"));
        country.setText(resourceBundle.getString("country"));





    }

    /**
     * This method is used to confirm the user is authentic.
     * this method will also pass the user to a default customer-view form once the user is authenticated.
     * @param actionEvent
     * @throws IOException
     */
    public void signIn(ActionEvent actionEvent) throws IOException, SQLException {
        InitCon.openConnection();
        // check user is legit
        String username = UsernameTextFLD.getText();
        String password = PasswordTextFLD.getText();
        FileWriter input  = new FileWriter("login_activity.txt",true);
        UserDAO user = new UserDAO();

        if(user.validation(username, password)){
            //TODO: Implement a warning here when appointment is within 15 minutes.
            ObservableList<Appointments> appSoon = AppointmentDAO.getAppointment();


           for(Appointments current: appSoon){
               LocalDateTime warnp = current.getStart().plusMinutes(15);
               LocalDateTime warnm = current.getStart().minusMinutes(15);
               LocalDate startDate = LocalDate.from(current.getStart());
               LocalDate today = LocalDate.now();

               for(int i = 0; i < 15; i++ ){
                   LocalDateTime warning = LocalDateTime.now().plusMinutes(i).truncatedTo(ChronoUnit.MINUTES);
                   // easier than debugger. Dont judges
//                   System.out.println("warning "  + warning);
//                   System.out.println("Current " + current.getStart());
                   if(current.getStart().isEqual(warning)){
                       Alert appointmentSoon = new Alert(Alert.AlertType.INFORMATION, "You have an upcoming appointment with ID: " + current.getAppointmentID() + " soon. Please check the appointments panel.", ButtonType.OK);
                       appointmentSoon.showAndWait();
                   }
               }
            }

            try {
                input.write("Username: " + username + " Status: Successful" + " Time: " + LocalDateTime.now() + "\n");
                System.out.println("writing succesful login");
                input.flush();
            }catch (IOException e){
                System.out.println("no soup for you");
            }
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("customer-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer View");
            stage.setScene(scene);
            stage.show();
        }
        else{
            try {
                //10 char to un
                input.write("Username: " + username + " Status: Failed " + " Time: " + LocalDateTime.now() + "\n");
                System.out.println("writing failed login");
                input.flush();
            }catch (IOException e){
                System.out.println("no soup for you");
            }

            Alert loginError = new Alert(Alert.AlertType.ERROR, "Incorrect username or password",ButtonType.OK);
            loginError.showAndWait();
        }


        // Move to the customer view




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
