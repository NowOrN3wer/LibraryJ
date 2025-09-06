package com.idb.microservicedemo.library.dto.auth;

public class LoginRequest {
    private String emailOrUserName;
    private String password;

    public LoginRequest(String emailOrUserName, String password) {
        this.emailOrUserName = emailOrUserName;
        this.password = password;
    }

    // Getters & Setters
    public String getEmailOrUserName() {
        return emailOrUserName;
    }

    public void setEmailOrUserName(String emailOrUserName) {
        this.emailOrUserName = emailOrUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
