package com.michael.c195_software2;

import java.time.LocalDateTime;

public class FirstLevelDivisions {
    private int divisionID;
    private String division;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    private int countryID;

    /**
     * Custom constructor for FLD
     * @param divisionID
     * @param division
     * @param created
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param countryID
     */
    public FirstLevelDivisions(int divisionID,String division,LocalDateTime created, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, int countryID){
        this.divisionID = divisionID;
        this.division = division;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.countryID = countryID;
    }

    /**
     * Empty Constructor for FLD
     */
    public  FirstLevelDivisions(){

    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDivision() {
        return division;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getCountryID() {
        return countryID;
    }
}

