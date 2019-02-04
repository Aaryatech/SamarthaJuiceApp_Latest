package com.ats.taborderingsystem.model;

public class AdminModel {

    private int adminId;
    private String username;
    private String password;
    private String type;
    private int delStatus;
    private String token;

    public AdminModel(int adminId, String username, String password, String type, int delStatus, String token) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.type = type;
        this.delStatus = delStatus;
        this.token = token;
    }

    public AdminModel(String username, String password, String type, int delStatus, String token) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.delStatus = delStatus;
        this.token = token;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AdminModel{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", delStatus=" + delStatus +
                ", token='" + token + '\'' +
                '}';
    }
}
