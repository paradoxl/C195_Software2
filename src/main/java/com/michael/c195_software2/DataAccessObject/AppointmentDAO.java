package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Appointments;
import com.michael.c195_software2.dataBaseConnection.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used as a bridge between the Database and the appointments class.
 */
public class AppointmentDAO {
    Appointments appointments = new Appointments();

    /**
     * This method returns information on appointments.
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointment() throws SQLException {
        ObservableList<Appointments> appList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println("Gathering appointments");

        //UTC conversion
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime conv = now.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = conv.withZoneSameInstant(ZoneOffset.UTC);

        int difference = utc.getHour() - now.getHour();

        while (rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String description = rs.getString("Description");
            String title = rs.getString("Title");
            String location =  rs.getString("Location");
            String type = rs.getString("Type");
//            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime().minusHours(difference);
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            LocalDateTime Created = rs.getTimestamp("Create_Date").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            start.format(formatter);
            DateFormat timeOnly  = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateOnly  = new SimpleDateFormat("MM:dd:yyyy");

            //Converting times to UTC then to LDT
            ZonedDateTime startConv = start.atZone(ZoneId.of("UTC")); // set LDT to ZDT with utc timezone
            ZonedDateTime startLDT = startConv.withZoneSameInstant(ZoneId.systemDefault()); // convert UTC to local
            LocalDateTime startFinal = startLDT.toLocalDateTime(); // convert ZDT to LDT

            ZonedDateTime endConv = end.atZone(ZoneId.of("UTC"));
            ZonedDateTime endLDT = endConv.withZoneSameInstant(ZoneId.systemDefault());
            LocalDateTime endFinal = endLDT.toLocalDateTime();

       Appointments adding = new Appointments(appointmentId,title,description,location,type,startFinal,endFinal,Created,customerID,userID,contactID);
        appList.add(adding);
            System.out.println("Appointments: " + appList);
        }
        return appList;
    }
}
