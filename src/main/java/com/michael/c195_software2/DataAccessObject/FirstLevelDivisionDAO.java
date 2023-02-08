package com.michael.c195_software2.DataAccessObject;

import com.michael.c195_software2.FirstLevelDivisions;
import com.michael.c195_software2.con.InitCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class is used as a bridge between the Database and first level division.
 */
public class FirstLevelDivisionDAO {

    /**
     * This method is used to return data on first level division
     * @return
     * @throws SQLException
     */
    public  static ObservableList<FirstLevelDivisions> getFLD() throws SQLException {
//        FirstLevelDivisions FLD = new FirstLevelDivisions();
        ObservableList<FirstLevelDivisions> FLD = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        System.out.println("FLD Query Executed");
        while (rs.next()) {
            int DivID = rs.getInt("Division_ID");
            String div = rs.getString("Division");
            LocalDateTime created = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdateBy = rs.getString("Last_Updated_By");
            int countryID = rs.getInt("Country_ID");

            FirstLevelDivisions firstLevel = new FirstLevelDivisions(DivID,div,created,createdBy,lastUpdate,lastUpdateBy,countryID);
            FLD.add(firstLevel);
        }

        return FLD;
    }
}
