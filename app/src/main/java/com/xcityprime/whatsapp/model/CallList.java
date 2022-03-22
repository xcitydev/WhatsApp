package com.xcityprime.whatsapp.model;

public class CallList {
    private String userID;
    private String username;
    private String date;
    private String urlProfile;
    private String callType;

    public CallList() {
    }

    public CallList(String userID, String username, String date, String urlProfile, String callType) {
        this.userID = userID;
        this.username = username;
        this.date = date;
        this.urlProfile = urlProfile;
        this.callType = callType;
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

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
