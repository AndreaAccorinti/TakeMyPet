package com.takemypet.service;

import com.takemypet.domain.*;
import com.takemypet.dto.request.RegisterRequest;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.UserAlreadyExistsException;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Handles new user registration. Creates the correct entity type based on the requested role.
 */
@Service
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtenteMapper utenteMapper;

    public RegistrationService(
            UtenteRepository utenteRepository,
            PasswordEncoder passwordEncoder,
            UtenteMapper utenteMapper) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.utenteMapper = utenteMapper;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        assertUsernameIsAvailable(request.getUsername());

        UtenteApp newUser = buildUser(request);
        utenteRepository.save(newUser);

        log.info("Registered new '{}' user with username '{}'", request.getTipo(), request.getUsername());
        return utenteMapper.toResponse(newUser);
    }

    private void assertUsernameIsAvailable(String username) {
        if (utenteRepository.existsById(username)) {
            throw new UserAlreadyExistsException(username);
        }
    }

    private UtenteApp buildUser(RegisterRequest request) {
        UtenteApp user = createUserOfRequestedType(request.getTipo());
        populateCommonFields(user, request);
        populateAppUserFields(user, request);
        return user;
    }

    private UtenteApp createUserOfRequestedType(String tipo) {
        return switch (tipo) {
            case "proprietario" -> new Proprietario();
            case "petsitter"    -> new PetSitter();
            default -> throw new IllegalArgumentException("Unknown user type: " + tipo);
        };
    }

    private void populateCommonFields(Utente user, RegisterRequest request) {
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setDataDiNascita(request.getDataDiNascita());
        user.setDataRegistrazione(LocalDate.now());

        Email email = new Email(request.getEmail());
        user.addEmail(email);
    }

    private void populateAppUserFields(UtenteApp user, RegisterRequest request) {
        user.setDescrizione(request.getDescrizione());
        user.setLatitudine(request.getLatitudine());
        user.setLongitudine(request.getLongitudine());
        user.setDoppioProfilo(request.isDoppioProfilo());
        user.setAttivo(true);
        user.setBloccato(false);
    }
}
