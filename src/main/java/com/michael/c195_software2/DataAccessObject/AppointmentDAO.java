package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Appointments;
import com.michael.c195_software2.con.InitCon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentDAO {
    Appointments appointments = new Appointments();
    public void getAppointment() throws SQLException {
        String query = "SELECT * FROM appointments";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            appointments.setAppointmentID(rs.getInt("Appointment_ID"));
            appointments.setDescription(rs.getString("Description"));
            appointments.setLocation(rs.getString("Location"));
            appointments.setStart(rs.getTimestamp("Start").toLocalDateTime());
            appointments.setEnd(rs.getTimestamp("End").toLocalDateTime());
            appointments.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
            appointments.setCustomerID(rs.getInt("Customer_ID"));
            appointments.setUserID(rs.getInt("User_ID"));
            appointments.setContactID(rs.getInt("Contact_ID"));

            int appointmentId = appointments.getAppointmentID();
            String description = appointments.getDescription();
            String location = appointments.getLocation();
            LocalDateTime start = appointments.getStart();
            LocalDateTime end = appointments.getEnd();
            LocalDateTime Created = appointments.getCreateDate();
            int customerID = appointments.getCustomerID();
            int userID = appointments.getUserID();
            int contactID = appointments.getContactID();
        }
    }
}
