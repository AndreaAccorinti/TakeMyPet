package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data for creating a community event")
public class CreateEventRequest {

    @NotBlank(message = "Event name is required")
    @Schema(description = "Name of the event", example = "Dog Park Meetup")
    private String nomeEvento;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    @Schema(description = "Date of the event", example = "2025-08-20")
    private LocalDate dataEvento;

    @NotBlank(message = "Description is required")
    @Schema(description = "Event description", example = "Bring your dogs!")
    private String descrizione;

    @NotBlank(message = "Organiser username is required")
    @Schema(description = "Username of the organiser", example = "mario_rossi")
    private String usernameOrganizzatore;

    @Schema(description = "GPS latitude", example = "41.9028")
    private String latitudine;

    @Schema(description = "GPS longitude", example = "12.4964")
    private String longitudine;
}
