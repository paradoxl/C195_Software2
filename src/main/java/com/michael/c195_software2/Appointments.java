package com.michael.c195_software2;

import java.time.LocalDateTime;

/**
 * This class is the backbone of the variables within appointments.
 * This class contains both a constructor and setters and getters.
 * This allows the code to be extended beyond creating an entirely new appointment the dev can simply change aspects.
 */
public class Appointments {
    private int appointmentID;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private int customerID;
    private int userID;
    private int contactID;


    public Appointments(int appointmentID, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, int customerID, int userID, int contactID){
        this.appointmentID = appointmentID;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this. createdBy = createdBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;

    }
    // TODO: check that we don't need values here.
    public Appointments(){

    }

    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }
    public int getAppointmentID(){
        return appointmentID;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return location;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public void setStart(LocalDateTime start){
        this.start = start;
    }
    public LocalDateTime getStart(){
        return start;
    }
    public void setEnd(LocalDateTime end){
        this.end = end;
    }
    public LocalDateTime getEnd(){
        return end;
    }
    public void setCreateDate(LocalDateTime createDate){
        this.createDate = createDate;
    }
    public LocalDateTime getCreateDate(){
        return createDate;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public void setCustomerID(){
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getContactID() {
        return contactID;
    }

}
