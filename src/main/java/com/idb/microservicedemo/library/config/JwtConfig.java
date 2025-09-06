package com.idb.microservicedemo.library.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey() {
        // jjwt kendi güvenli 512-bit HS512 key’ini üretir
        return Jwts.SIG.HS512.key().build();
    }
}
