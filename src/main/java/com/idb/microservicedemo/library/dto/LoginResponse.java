package com.idb.microservicedemo.library.dto;

import java.time.Instant;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private Instant refreshTokenExpires;

    public LoginResponse(String token, String refreshToken, Instant refreshTokenExpires) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.refreshTokenExpires = refreshTokenExpires;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getRefreshTokenExpires() {
        return refreshTokenExpires;
    }
}
