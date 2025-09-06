package com.idb.microservicedemo.library.service.auth.imp;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.domain.entities.appuser.AppUser;
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

    @Override
    @Transactional
    public Result<LoginResponse> refresh(String refreshToken) {
        // 0) Basit input kontrolü
        if (refreshToken == null || refreshToken.isBlank()) {
            return Result.failure(400, "Refresh token boş olamaz");
        }

        // 1) Refresh token ile kullanıcıyı bul
        AppUser user = userRepository.findByRefreshToken(refreshToken).orElse(null);
        if (user == null) {
            return Result.failure(401, "Invalid or expired refresh token");
        }

        // 2) Süre kontrolü
        OffsetDateTime now = OffsetDateTime.now();
        if (user.getRefreshTokenExpires() == null || !user.getRefreshTokenExpires().isAfter(now)) {
            return Result.failure(401, "Invalid or expired refresh token");
        }

        // (İsteğe bağlı) hesap kilidi / doğrulama kontrolleri
        if (!user.isAccountNonLocked()) {
            return Result.failure(403, "Hesap kilitli");
        }

        // 3) Yeni access token üret
        String newAccessToken = jwtTokenProvider.generateToken(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername()
        );

        // 4) Refresh token'ı tek kullanımlık yap: rotate et
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();
        OffsetDateTime newRefreshExpiry = now.plusDays(7); // politikan neyse

        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpires(newRefreshExpiry);
        userRepository.save(user);

        // 5) Başarı sonucu
        LoginResponse payload = new LoginResponse(
                newAccessToken,
                newRefreshToken,
                newRefreshExpiry.toInstant()
        );
        return Result.succeed(payload);
    }

}
