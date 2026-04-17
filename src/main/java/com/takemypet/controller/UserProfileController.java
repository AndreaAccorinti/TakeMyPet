package com.takemypet.controller;

import com.takemypet.dto.response.UserResponse;
import com.takemypet.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users/{username}/profile")
@Tag(name = "User Profile", description = "View and update user profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @Operation(summary = "Get profile", description = "Retrieve a user's public profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getProfile(
            @Parameter(description = "Username", required = true) @PathVariable String username) {
        return ResponseEntity.ok(userProfileService.getProfile(username));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile image", description = "Upload a profile picture as a multipart file.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image uploaded"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> uploadProfileImage(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userProfileService.updateProfileImage(username, file));
    }

    @PostMapping(value = "/image/base64", consumes = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Upload profile image (Base64)", description = "Upload a profile picture encoded as Base64.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image uploaded"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> uploadProfileImageFromBase64(
            @PathVariable String username,
            @RequestBody String base64Data) throws IOException {
        return ResponseEntity.ok(userProfileService.updateProfileImageFromBase64(username, base64Data));
    }
}
