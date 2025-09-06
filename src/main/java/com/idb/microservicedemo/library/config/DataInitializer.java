package com.idb.microservicedemo.library.config;

import com.idb.microservicedemo.library.domain.AppUser;
import com.idb.microservicedemo.library.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createFirstUser(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";

            if (userRepository.existsByUsername(adminUsername)) {
                return; // admin varsa tekrar ekleme
            }

            AppUser admin = AppUser.builder()
                    .username(adminUsername)
                    .email("admin@admin.com")
                    .firstName("admin")
                    .lastName("admin")
                    .password(passwordEncoder.encode("1")) // şifre encode edilmeli
                    .emailConfirmed(true)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Default admin user created: " + admin.getUsername());
        };
    }
}
