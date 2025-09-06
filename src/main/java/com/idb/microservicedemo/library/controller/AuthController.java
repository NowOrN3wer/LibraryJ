package com.idb.microservicedemo.library.controller;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.domain.AppUser;
import com.idb.microservicedemo.library.dto.LoginRequest;
import com.idb.microservicedemo.library.dto.LoginResponse;
import com.idb.microservicedemo.library.repository.AppUserRepository;
import com.idb.microservicedemo.library.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtTokenProvider jwtTokenProvider,
                          AppUserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request,
                                       HttpServletResponse resp) {
        AppUser user = userRepository.findByUsername(request.getEmailOrUserName())
                .or(() -> userRepository.findByEmail(request.getEmailOrUserName()))
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value()); // <-- HTTP 401
            return Result.failure(401, "Invalid credentials"); // body aynı kalır
        }

        String token = jwtTokenProvider.generateToken(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken();
        OffsetDateTime refreshExpiry = OffsetDateTime.now().plusMinutes(180);

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpires(refreshExpiry);
        userRepository.save(user);

        return Result.succeed(new LoginResponse(
                token, refreshToken, refreshExpiry.toInstant()
        ));
    }
}
