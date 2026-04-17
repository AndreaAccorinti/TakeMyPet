package com.takemypet.service;

import com.takemypet.domain.Immagine;
import com.takemypet.domain.UtenteApp;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.exception.ResourceNotFoundException;
import com.takemypet.mapper.UtenteMapper;
import com.takemypet.repository.UtenteAppRepository;
import com.takemypet.util.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Manages user profile retrieval and profile image updates.
 */
@Service
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UtenteAppRepository utenteAppRepository;
    private final UtenteMapper utenteMapper;
    private final ImageStorageService imageStorageService;

    public UserProfileService(
            UtenteAppRepository utenteAppRepository,
            UtenteMapper utenteMapper,
            ImageStorageService imageStorageService) {
        this.utenteAppRepository = utenteAppRepository;
        this.utenteMapper = utenteMapper;
        this.imageStorageService = imageStorageService;
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(String username) {
        UtenteApp user = findUserOrThrow(username);
        return utenteMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateProfileImage(String username, MultipartFile file) throws IOException {
        UtenteApp user = findUserOrThrow(username);
        String imageUrl = imageStorageService.saveProfileImage(username, file);
        setProfileImageUrl(user, imageUrl);
        utenteAppRepository.save(user);
        log.info("Profile image updated for user '{}'", username);
        return utenteMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateProfileImageFromBase64(String username, String base64Data) throws IOException {
        UtenteApp user = findUserOrThrow(username);
        String imageUrl = imageStorageService.saveProfileImageFromBase64(username, base64Data);
        setProfileImageUrl(user, imageUrl);
        utenteAppRepository.save(user);
        log.info("Profile image updated (Base64) for user '{}'", username);
        return utenteMapper.toResponse(user);
    }

    private void setProfileImageUrl(UtenteApp user, String imageUrl) {
        if (user.getImmagineProfilo() == null) {
            user.setImmagineProfilo(new Immagine(imageUrl));
        } else {
            user.getImmagineProfilo().setUrlImmagine(imageUrl);
        }
    }

    private UtenteApp findUserOrThrow(String username) {
        return utenteAppRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }
}
