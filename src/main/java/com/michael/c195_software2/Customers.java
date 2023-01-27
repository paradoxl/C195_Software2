package com.michael.c195_software2;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customers {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private int divisionID;


    public Customers(int customerID, String customerName, String address, String postalCode,int divisionID ){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.divisionID = divisionID;
    }
    public Customers(){

    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public int getDivisionID() {
        return divisionID;
    }
}
