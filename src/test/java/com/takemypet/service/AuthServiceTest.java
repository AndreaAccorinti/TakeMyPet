package com.takemypet.service;

import com.takemypet.config.AppProperties;
import com.takemypet.domain.Email;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.LoginRequest;
import com.takemypet.dto.request.UnlockAccountRequest;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.InvalidCredentialsException;
import com.takemypet.exception.InvalidUnlockCodeException;
import com.takemypet.exception.UserBlockedException;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.UtenteRepository;
import com.takemypet.util.UnlockCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @Mock private UtenteRepository utenteRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UnlockCodeGenerator unlockCodeGenerator;
    @Mock private EmailService emailService;
    @Mock private UtenteMapper utenteMapper;
    @Mock private AppProperties appProperties;
    @Mock private AppProperties.Security security;

    @InjectMocks
    private AuthService authService;

    private Proprietario activeUser;

    @BeforeEach
    void setUp() {
        activeUser = new Proprietario();
        activeUser.setUsername("mario_rossi");
        activeUser.setPassword("$2a$12$hashed");
        activeUser.setNome("Mario");
        activeUser.setCognome("Rossi");
        activeUser.setBloccato(false);
        activeUser.setContatoreAccessiSbagliati(0);

        when(appProperties.getSecurity()).thenReturn(security);
        when(security.getMaxFailedLoginAttempts()).thenReturn(10);
    }

    @Test
    void login_withValidCredentials_returnsUserResponse() {
        LoginRequest request = loginRequest("mario_rossi", "password123");
        UserResponse expectedResponse = new UserResponse();

        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("password123", "$2a$12$hashed")).thenReturn(true);
        when(utenteRepository.save(any())).thenReturn(activeUser);
        when(utenteMapper.toResponse(activeUser)).thenReturn(expectedResponse);

        UserResponse result = authService.login(request);

        assertThat(result).isEqualTo(expectedResponse);
        assertThat(activeUser.getContatoreAccessiSbagliati()).isZero();
        verify(utenteRepository, times(2)).save(activeUser);
    }

    @Test
    void login_withWrongPassword_incrementsFailedCounter_andThrows() {
        LoginRequest request = loginRequest("mario_rossi", "wrong");

        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("wrong", "$2a$12$hashed")).thenReturn(false);
        when(utenteRepository.save(any())).thenReturn(activeUser);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        assertThat(activeUser.getContatoreAccessiSbagliati()).isEqualTo(1);
    }

    @Test
    void login_afterMaxFailedAttempts_blocksAccountAndSendsEmail() {
        activeUser.setContatoreAccessiSbagliati(9);
        Email email = new Email("mario@example.com");
        email.setUtente(activeUser);
        activeUser.setEmails(List.of(email));

        LoginRequest request = loginRequest("mario_rossi", "wrong");

        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(unlockCodeGenerator.generate()).thenReturn("TMP-12345");
        when(utenteRepository.save(any())).thenReturn(activeUser);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UserBlockedException.class);

        assertThat(activeUser.isBloccato()).isTrue();
        assertThat(activeUser.getCodiceSblocco()).isEqualTo("TMP-12345");
        verify(emailService).sendAccountUnlockCode("mario@example.com", "TMP-12345");
    }

    @Test
    void login_withBlockedAccount_throwsUserBlockedException() {
        activeUser.setBloccato(true);
        LoginRequest request = loginRequest("mario_rossi", "password123");

        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));
        when(utenteRepository.save(any())).thenReturn(activeUser);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UserBlockedException.class);
    }

    @Test
    void login_withUnknownUsername_throwsInvalidCredentialsException() {
        LoginRequest request = loginRequest("unknown", "password");
        when(utenteRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void unlockAccount_withValidCode_unlocksAndReturnsUser() {
        activeUser.setBloccato(true);
        activeUser.setCodiceSblocco("TMP-12345");

        UnlockAccountRequest request = new UnlockAccountRequest();
        request.setUsername("mario_rossi");
        request.setCodiceSblocco("TMP-12345");

        UserResponse expectedResponse = new UserResponse();
        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));
        when(utenteRepository.save(any())).thenReturn(activeUser);
        when(utenteMapper.toResponse(activeUser)).thenReturn(expectedResponse);

        UserResponse result = authService.unlockAccount(request);

        assertThat(result).isEqualTo(expectedResponse);
        assertThat(activeUser.isBloccato()).isFalse();
        assertThat(activeUser.getCodiceSblocco()).isNull();
    }

    @Test
    void unlockAccount_withWrongCode_throwsInvalidUnlockCodeException() {
        activeUser.setBloccato(true);
        activeUser.setCodiceSblocco("TMP-12345");

        UnlockAccountRequest request = new UnlockAccountRequest();
        request.setUsername("mario_rossi");
        request.setCodiceSblocco("TMP-99999");

        when(utenteRepository.findById("mario_rossi")).thenReturn(Optional.of(activeUser));

        assertThatThrownBy(() -> authService.unlockAccount(request))
                .isInstanceOf(InvalidUnlockCodeException.class);
    }

    private LoginRequest loginRequest(String username, String password) {
        LoginRequest req = new LoginRequest();
        req.setUsername(username);
        req.setPassword(password);
        return req;
    }
}
