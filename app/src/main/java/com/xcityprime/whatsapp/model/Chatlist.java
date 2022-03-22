package com.xcityprime.whatsapp.model;

public class Chatlist {
    private String userID;
    private String username;
    private String description;
    private String date;
    private String urlProfile;

    public Chatlist(String userID, String username, String description, String date, String urlProfile) {
        this.userID = userID;
        this.username = username;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
