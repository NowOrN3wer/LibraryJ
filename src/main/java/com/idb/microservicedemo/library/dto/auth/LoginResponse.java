package com.idb.microservicedemo.library.dto.auth;

import java.time.Instant;

public record LoginResponse(String token, String refreshToken, Instant refreshTokenExpires) {
}
