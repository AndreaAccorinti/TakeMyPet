package com.takemypet.service;

import com.takemypet.config.AppProperties;
import com.takemypet.domain.Email;
import com.takemypet.domain.Utente;
import com.takemypet.domain.UtenteApp;
import com.takemypet.dto.request.LoginRequest;
import com.takemypet.dto.request.UnlockAccountRequest;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.InvalidCredentialsException;
import com.takemypet.exception.InvalidUnlockCodeException;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.exception.UserBlockedException;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.UtenteRepository;
import com.takemypet.util.UnlockCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Handles authentication: login, failed-attempt tracking, and account unlock.
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final UnlockCodeGenerator unlockCodeGenerator;
    private final EmailService emailService;
    private final UtenteMapper utenteMapper;
    private final AppProperties appProperties;

    public AuthService(
            UtenteRepository utenteRepository,
            PasswordEncoder passwordEncoder,
            UnlockCodeGenerator unlockCodeGenerator,
            EmailService emailService,
            UtenteMapper utenteMapper,
            AppProperties appProperties) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.unlockCodeGenerator = unlockCodeGenerator;
        this.emailService = emailService;
        this.utenteMapper = utenteMapper;
        this.appProperties = appProperties;
    }

    @Transactional
    public UserResponse login(LoginRequest request) {
        Utente utente = findUserOrThrow(request.getUsername());

        if (utente.isBloccato()) {
            throw new UserBlockedException(utente.getUsername());
        }

        updateLastLoginTimestamp(utente);

        if (passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            return handleSuccessfulLogin(utente);
        } else {
            return handleFailedLoginAttempt(utente);
        }
    }

    @Transactional
    public UserResponse unlockAccount(UnlockAccountRequest request) {
        Utente utente = findUserOrThrow(request.getUsername());

        if (!request.getCodiceSblocco().equals(utente.getCodiceSblocco())) {
            throw new InvalidUnlockCodeException();
        }

        utente.unblock();
        utenteRepository.save(utente);
        log.info("Account unlocked for user '{}'", utente.getUsername());
        return mapToResponse(utente);
    }

    private UserResponse handleSuccessfulLogin(Utente utente) {
        utente.resetFailedLoginAttempts();
        if (utente instanceof UtenteApp app) {
            app.setAttivo(true);
        }
        utenteRepository.save(utente);
        log.info("Successful login for user '{}'", utente.getUsername());
        return mapToResponse(utente);
    }

    private UserResponse handleFailedLoginAttempt(Utente utente) {
        utente.incrementFailedLoginAttempts();

        int maxAttempts = appProperties.getSecurity().getMaxFailedLoginAttempts();
        if (utente.getContatoreAccessiSbagliati() >= maxAttempts) {
            blockAccountAndSendUnlockEmail(utente);
            throw new UserBlockedException(utente.getUsername());
        }

        utenteRepository.save(utente);
        log.warn("Failed login attempt #{} for user '{}'",
                utente.getContatoreAccessiSbagliati(), utente.getUsername());
        throw new InvalidCredentialsException();
    }

    private void blockAccountAndSendUnlockEmail(Utente utente) {
        String unlockCode = unlockCodeGenerator.generate();
        utente.block(unlockCode);
        utenteRepository.save(utente);

        utente.getEmails().forEach(email ->
                emailService.sendAccountUnlockCode(email.getEmail(), unlockCode));

        log.warn("Account '{}' blocked after {} failed attempts", utente.getUsername(),
                utente.getContatoreAccessiSbagliati());
    }

    private void updateLastLoginTimestamp(Utente utente) {
        utente.setDataOraUltimoLogin(LocalDateTime.now());
        utenteRepository.save(utente);
    }

    private Utente findUserOrThrow(String username) {
        return utenteRepository.findById(username)
                .orElseThrow(() -> new InvalidCredentialsException());
    }

    private UserResponse mapToResponse(Utente utente) {
        if (utente instanceof UtenteApp app) {
            return utenteMapper.toResponse(app);
        }
        return utenteMapper.toResponse(utente);
    }
}
