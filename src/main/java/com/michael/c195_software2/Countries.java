package com.michael.c195_software2;

import java.time.LocalDateTime;

/**
 * This class hold all information regarding specific countries.
 */
public class Countries {
    private int countryID;
    private String country;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;

    /**
     * Custom constructor
     * @param countryID
     * @param country
     * @param created
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     */
    public Countries(int countryID, String country, LocalDateTime created, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy){
        this.countryID = countryID;
        this.country = country;
        this.created = created;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;

    }

    /**
     * Blank Constructor
     */
    public Countries(){}



    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getCreated() {
        return created;
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

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
