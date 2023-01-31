package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.Countries;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CountryDAO {
    public static ObservableList<Countries> getCountries() throws SQLException{
        ObservableList<Countries> countries = FXCollections.observableArrayList();
        String query = "SELECT * FROM countries";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println("Country Query Executed");

        while (rs.next()){
            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("Country");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdateBy = rs.getString("Last_Updated_By");
            Countries countryObjcet = new Countries();
            countryObjcet.setCountryID(countryID);
            countryObjcet.setCountry(country);
            countryObjcet.setCreated(createDate);
            countryObjcet.setCreatedBy(createdBy);
            countryObjcet.setLastUpdate(lastUpdate);
            countryObjcet.setLastUpdateBy(lastUpdateBy);

            countries.add(countryObjcet);

        }

        return countries;
    }
}
