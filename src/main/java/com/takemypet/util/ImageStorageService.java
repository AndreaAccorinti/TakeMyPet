package com.takemypet.util;

import com.takemypet.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Handles saving images to disk and returning their public URL.
 * Supports both multipart upload and Base64-encoded payloads.
 */
@Service
public class ImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(ImageStorageService.class);

    private final AppProperties appProperties;

    public ImageStorageService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String saveProfileImage(String username, MultipartFile file) throws IOException {
        String filename = username + ".jpg";
        Path storageDir = Paths.get(appProperties.getImage().getStorageDir());
        Files.createDirectories(storageDir);

        Path destination = storageDir.resolve(filename);
        file.transferTo(destination);

        log.info("Saved profile image for user '{}' to {}", username, destination);
        return appProperties.getImage().getBaseUrl() + filename;
    }

    public String saveProfileImageFromBase64(String username, String base64Data) throws IOException {
        byte[] imageBytes = decodeBase64Image(base64Data);
        String filename = username + ".jpg";
        Path storageDir = Paths.get(appProperties.getImage().getStorageDir());
        Files.createDirectories(storageDir);

        Path destination = storageDir.resolve(filename);
        Files.write(destination, imageBytes);

        log.info("Saved Base64 profile image for user '{}' to {}", username, destination);
        return appProperties.getImage().getBaseUrl() + filename;
    }

    private byte[] decodeBase64Image(String base64Data) {
        String sanitized = base64Data.replaceAll("\\s+", "");
        if (sanitized.contains(",")) {
            sanitized = sanitized.substring(sanitized.indexOf(',') + 1);
        }
        return Base64.getDecoder().decode(sanitized);
    }
}
