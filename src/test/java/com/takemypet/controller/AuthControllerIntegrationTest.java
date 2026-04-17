package com.takemypet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takemypet.domain.Email;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.LoginRequest;
import com.takemypet.dto.request.UnlockAccountRequest;
import com.takemypet.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Proprietario user = new Proprietario();
        user.setUsername("mario_rossi");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setNome("Mario");
        user.setCognome("Rossi");
        user.setDataRegistrazione(LocalDate.now());
        user.setBloccato(false);
        user.setAttivo(true);

        Email email = new Email("mario@example.com");
        user.addEmail(email);

        utenteRepository.save(user);
    }

    @Test
    void login_withValidCredentials_returns200WithUserProfile() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("mario_rossi");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mario_rossi"))
                .andExpect(jsonPath("$.nome").value("Mario"));
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("mario_rossi");
        request.setPassword("wrong_password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withUnknownUser_returns401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("nobody");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withMissingFields_returns400WithValidationError() throws Exception {
        LoginRequest request = new LoginRequest();
        // username and password intentionally blank

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void unlock_withValidCode_returns200() throws Exception {
        Proprietario user = (Proprietario) utenteRepository.findById("mario_rossi").get();
        user.setBloccato(true);
        user.setCodiceSblocco("TMP-12345");
        utenteRepository.save(user);

        UnlockAccountRequest request = new UnlockAccountRequest();
        request.setUsername("mario_rossi");
        request.setCodiceSblocco("TMP-12345");

        mockMvc.perform(post("/api/auth/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mario_rossi"));
    }

    @Test
    void unlock_withInvalidCode_returns400() throws Exception {
        Proprietario user = (Proprietario) utenteRepository.findById("mario_rossi").get();
        user.setBloccato(true);
        user.setCodiceSblocco("TMP-12345");
        utenteRepository.save(user);

        UnlockAccountRequest request = new UnlockAccountRequest();
        request.setUsername("mario_rossi");
        request.setCodiceSblocco("TMP-WRONG");

        mockMvc.perform(post("/api/auth/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
