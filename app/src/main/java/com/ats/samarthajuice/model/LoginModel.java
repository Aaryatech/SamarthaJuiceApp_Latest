package com.ats.samarthajuice.model;

public class LoginModel {

    private String message;
    private boolean error;
    private Admin admin;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "message='" + message + '\'' +
                ", error=" + error +
                ", admin=" + admin +
                '}';
    }
}
