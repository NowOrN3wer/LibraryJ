package com.idb.microservicedemo.library.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final MacAlgorithm algorithm = Jwts.SIG.HS512; // ✅ deprecated değil

    public JwtTokenProvider(SecretKey key) {
        this.key = key;
    }

    public String generateToken(String id, String name, String email, String userName) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600); // 1 saat geçerli

        return Jwts.builder()
                .claim("Id", id)
                .claim("Name", name)
                .claim("Email", email)
                .claim("UserName", userName)
                .issuer("LibraryBackEnd")
                .audience().add("LibraryFrontEnd").and()
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key, algorithm) // ✅ güncel imzalama
                .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}