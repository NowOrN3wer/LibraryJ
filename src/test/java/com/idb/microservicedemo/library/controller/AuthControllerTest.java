package com.idb.microservicedemo.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idb.microservicedemo.library.domain.entities.appuser.AppUser;
import com.idb.microservicedemo.library.dto.auth.LoginRequest;
import com.idb.microservicedemo.library.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.bootstrap.create-admin=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false" // istersen
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.flush();

        AppUser user = AppUser.builder()
                .username("admin")
                .email("admin@admin.com")
                .firstName("admin")
                .lastName("admin")
                .password(passwordEncoder.encode("1234"))
                .emailConfirmed(true)
                .build();

        userRepository.saveAndFlush(user);
    }

    @Test
    void login_withValidCredentials_returnsTokenAndRefreshToken() throws Exception {
        var loginRequest = new LoginRequest("admin", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }

    @Test
    void login_withInvalidCredentials_returns401() throws Exception {
        var loginRequest = new LoginRequest("admin", "wrongpass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessages[0]").value("Invalid credentials"));
    }
}
