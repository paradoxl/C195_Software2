package com.michael.c195_software2;



public class Customers {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private int divisionID;


    /**
     * Custom constructor
     * @param customerID
     * @param customerName
     * @param address
     * @param postalCode
     * @param divisionID
     */
    public Customers(int customerID, String customerName, String address, String postalCode,int divisionID ){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.divisionID = divisionID;
    }

    /**
     * custom empty constructor
     */
    public Customers(){

    }

    /**
     * set customer id
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * get customer id
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * set customer name
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * get customer name
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * set address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * get address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * set postal code
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * get postal code
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * set division id
     * @param divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * get division id
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }
}
