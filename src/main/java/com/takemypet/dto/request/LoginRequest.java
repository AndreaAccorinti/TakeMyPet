package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credentials for user login")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Unique username", example = "mario_rossi")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "secret123")
    private String password;
}
