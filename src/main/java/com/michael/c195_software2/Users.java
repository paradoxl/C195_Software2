package com.michael.c195_software2;

/**
 * This class is used to hold User data
 */
public class Users {
    private int userID;
    private String username;
    private String password;

    public Users(){

    }

    /**
     * Set userID
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Get useID
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Set Password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get Password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * set Username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get Username
     * @return
     */
    public String getUsername() {
        return username;
    }
}
