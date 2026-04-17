package com.takemypet.service;

import com.takemypet.domain.PetSitter;
import com.takemypet.domain.Proprietario;
import com.takemypet.dto.request.RegisterRequest;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.UserAlreadyExistsException;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock private UtenteRepository utenteRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UtenteMapper utenteMapper;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_proprietario_createsProprietarioAndReturnsResponse() {
        RegisterRequest request = registerRequest("mario_rossi", "proprietario");
        UserResponse expected = new UserResponse();

        when(utenteRepository.existsById("mario_rossi")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$hashed");
        when(utenteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(utenteMapper.toResponse(any(com.takemypet.domain.UtenteApp.class))).thenReturn(expected);

        UserResponse result = registrationService.register(request);

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<com.takemypet.domain.UtenteApp> captor =
                ArgumentCaptor.forClass(com.takemypet.domain.UtenteApp.class);
        verify(utenteRepository).save(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(Proprietario.class);
        assertThat(captor.getValue().getUsername()).isEqualTo("mario_rossi");
        assertThat(captor.getValue().getPassword()).isEqualTo("$2a$hashed");
    }

    @Test
    void register_petsitter_createsPetSitter() {
        RegisterRequest request = registerRequest("luca_ps", "petsitter");

        when(utenteRepository.existsById("luca_ps")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(utenteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(utenteMapper.toResponse(any(com.takemypet.domain.UtenteApp.class))).thenReturn(new UserResponse());

        registrationService.register(request);

        ArgumentCaptor<com.takemypet.domain.UtenteApp> captor =
                ArgumentCaptor.forClass(com.takemypet.domain.UtenteApp.class);
        verify(utenteRepository).save(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(PetSitter.class);
    }

    @Test
    void register_withExistingUsername_throwsUserAlreadyExistsException() {
        RegisterRequest request = registerRequest("existing", "proprietario");
        when(utenteRepository.existsById("existing")).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existing");
    }

    @Test
    void register_passwordIsHashedBeforeSaving() {
        RegisterRequest request = registerRequest("user1", "petsitter");
        request.setPassword("plaintext");

        when(utenteRepository.existsById(anyString())).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("$bcrypt$hashed");
        when(utenteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(utenteMapper.toResponse(any(com.takemypet.domain.UtenteApp.class))).thenReturn(new UserResponse());

        registrationService.register(request);

        ArgumentCaptor<com.takemypet.domain.UtenteApp> captor =
                ArgumentCaptor.forClass(com.takemypet.domain.UtenteApp.class);
        verify(utenteRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("$bcrypt$hashed");
        assertThat(captor.getValue().getPassword()).doesNotContain("plaintext");
    }

    private RegisterRequest registerRequest(String username, String tipo) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setPassword("password123");
        req.setNome("Test");
        req.setCognome("User");
        req.setEmail("test@example.com");
        req.setTipo(tipo);
        return req;
    }
}
