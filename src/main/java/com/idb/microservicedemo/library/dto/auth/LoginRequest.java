package com.idb.microservicedemo.library.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getters & Setters
    private String emailOrUserName;
    private String password;

    public LoginRequest(String emailOrUserName, String password) {
        this.emailOrUserName = emailOrUserName;
        this.password = password;
    }
}
