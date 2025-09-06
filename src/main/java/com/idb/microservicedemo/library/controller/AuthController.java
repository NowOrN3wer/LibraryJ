package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.auth.LoginRequest;
import com.idb.microservicedemo.library.dto.auth.LoginResponse;
import com.idb.microservicedemo.library.dto.auth.RefreshRequest;
import com.idb.microservicedemo.library.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request,
                                       HttpServletResponse resp) {
        var result = authService.login(request.getEmailOrUserName(), request.getPassword());
        resp.setStatus(result.getStatusCode()); // 200/401 vs.
        return result;
    }

    @PostMapping("/refresh")
    public Result<LoginResponse> refresh(@RequestBody RefreshRequest request, HttpServletResponse resp) {
        Result<LoginResponse> result = authService.refresh(request.refreshToken());
        resp.setStatus(result.getStatusCode()); // 200/401 vs.
        return result;
    }
}