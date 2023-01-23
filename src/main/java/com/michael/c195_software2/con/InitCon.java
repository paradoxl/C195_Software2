package com.michael.c195_software2.con;

import java.sql.Connection;
import java.sql.DriverManager;

public class InitCon {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Flux Capacitor Charged");
        }
        catch(Exception e)
        {
            System.out.println("Charge Failed " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Powering down warp core!");
        }
        catch(Exception e)
        {
            System.out.println("Warp core breach! " + e.getMessage());
        }
    }
}
