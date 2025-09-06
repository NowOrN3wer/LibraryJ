package com.idb.microservicedemo.library.service.auth.imp;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.auth.LoginResponse;
import com.idb.microservicedemo.library.repository.AppUserRepository;
import com.idb.microservicedemo.library.security.JwtTokenProvider;
import com.idb.microservicedemo.library.service.auth.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
                           AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Result<LoginResponse> login(String emailOrUsername, String password) {
        var user = userRepository.findByUsername(emailOrUsername)
                .or(() -> userRepository.findByEmail(emailOrUsername))
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return Result.failure(401, "Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken();
        var refreshExpiry = OffsetDateTime.now().plusMinutes(180);

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpires(refreshExpiry);
        userRepository.save(user);

        return Result.succeed(new LoginResponse(
                token, refreshToken, refreshExpiry.toInstant()
        ));
    }
}
