package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "New user registration data")
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username may only contain letters, digits, and underscores")
    @Schema(description = "Unique username", example = "mario_rossi")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Password (min 8 chars)", example = "secret123")
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "First name", example = "Mario")
    private String nome;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name", example = "Rossi")
    private String cognome;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Schema(description = "Email address", example = "mario.rossi@example.com")
    private String email;

    @NotBlank(message = "User type is required")
    @Pattern(regexp = "proprietario|petsitter", message = "Type must be 'proprietario' or 'petsitter'")
    @Schema(description = "User type", allowableValues = {"proprietario", "petsitter"}, example = "proprietario")
    private String tipo;

    @Schema(description = "Date of birth", example = "1990-05-15")
    private LocalDate dataDiNascita;

    @Schema(description = "Profile description", example = "I love animals!")
    private String descrizione;

    @Schema(description = "GPS latitude", example = "41.9028")
    private String latitudine;

    @Schema(description = "GPS longitude", example = "12.4964")
    private String longitudine;

    @Schema(description = "Register as both owner and pet sitter", example = "false")
    private boolean doppioProfilo = false;
}
