package com.takemypet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.RegisterRequest;
import com.takemypet.repository.UtenteRepository;
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
class RegistrationControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void register_withValidRequest_returns201() throws Exception {
        RegisterRequest request = validRegisterRequest("new_user", "proprietario");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new_user"))
                .andExpect(jsonPath("$.tipoUtente").value("proprietario"));
    }

    @Test
    void register_petsitter_returns201WithCorrectType() throws Exception {
        RegisterRequest request = validRegisterRequest("new_sitter", "petsitter");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoUtente").value("petsitter"));
    }

    @Test
    void register_withExistingUsername_returns409() throws Exception {
        Proprietario existing = new Proprietario();
        existing.setUsername("taken");
        existing.setPassword(passwordEncoder.encode("pass"));
        existing.setNome("A");
        existing.setCognome("B");
        existing.setDataRegistrazione(LocalDate.now());
        utenteRepository.save(existing);

        RegisterRequest request = validRegisterRequest("taken", "proprietario");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void register_withInvalidEmail_returns400() throws Exception {
        RegisterRequest request = validRegisterRequest("user2", "proprietario");
        request.setEmail("not-an-email");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void register_withShortPassword_returns400() throws Exception {
        RegisterRequest request = validRegisterRequest("user3", "proprietario");
        request.setPassword("short");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private RegisterRequest validRegisterRequest(String username, String tipo) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setPassword("strongPass1");
        req.setNome("Test");
        req.setCognome("User");
        req.setEmail("test@example.com");
        req.setTipo(tipo);
        return req;
    }
}
