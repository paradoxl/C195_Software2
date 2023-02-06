package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Appointments;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDAO {
    Appointments appointments = new Appointments();

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int difference = utc.getHour() - now.getHour();

        while (rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String description = rs.getString("Description");
            String title = rs.getString("Title");
            String location =  rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime().minusHours(difference);
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime().minusHours(difference);
            LocalDateTime Created = rs.getTimestamp("Create_Date").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

       Appointments adding = new Appointments(appointmentId,title,description,location,type,start,end,Created,customerID,userID,contactID);
        appList.add(adding);
            System.out.println(appList);
        }
        return appList;
    }
}
