package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.auth.LoginRequest;
import com.idb.microservicedemo.library.dto.auth.LoginResponse;
import com.idb.microservicedemo.library.dto.auth.RefreshRequest;
import com.idb.microservicedemo.library.service.auth.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmailOrUserName(), request.getPassword());
    }

    @PostMapping("/refresh")
    public Result<LoginResponse> refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }
}