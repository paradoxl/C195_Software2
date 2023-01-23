package com.michael.c195_software2.Connection;


public abstract class Connection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcURL = protocol + vendor + location + databaseName +"?connectionTimeZone = SERVER"; //local
    private static final String Driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    public static Connection connection;


    public static void openConnection(){
        try{
            Class.forName(Driver);
            connection = DriverManager.getConnection(jdbcURL,userName,password);
            System.out.println("flux capacitor chargerd");
        } catch (ClassNotFoundException e) {
            System.out.println("Charge failed" + e);
        }
    }


}
