package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "User profile information")
public class UserResponse {

    @Schema(description = "Unique username", example = "mario_rossi")
    private String username;

    @Schema(description = "First name", example = "Mario")
    private String nome;

    @Schema(description = "Last name", example = "Rossi")
    private String cognome;

    @Schema(description = "User type", example = "proprietario")
    private String tipoUtente;

    @Schema(description = "Whether the account is blocked")
    private boolean bloccato;

    @Schema(description = "Whether the user is currently active")
    private boolean attivo;

    @Schema(description = "Profile description")
    private String descrizione;

    @Schema(description = "GPS latitude")
    private String latitudine;

    @Schema(description = "GPS longitude")
    private String longitudine;

    @Schema(description = "URL of the profile image")
    private String immagineProfilo;

    @Schema(description = "Date of birth")
    private LocalDate dataDiNascita;

    @Schema(description = "Registration date")
    private LocalDate dataRegistrazione;

    @Schema(description = "Last login timestamp")
    private LocalDateTime dataOraUltimoLogin;
}
