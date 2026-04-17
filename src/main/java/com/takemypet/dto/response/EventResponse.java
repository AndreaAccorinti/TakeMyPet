package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Community event")
public class EventResponse {

    @Schema(description = "Event ID")
    private Long id;

    @Schema(description = "Event name")
    private String nomeEvento;

    @Schema(description = "Organiser username")
    private String usernameOrganizzatore;

    @Schema(description = "Event date")
    private LocalDate dataEvento;

    @Schema(description = "Event description")
    private String descrizione;

    @Schema(description = "GPS latitude")
    private String latitudine;

    @Schema(description = "GPS longitude")
    private String longitudine;

    @Schema(description = "Usernames of participants")
    private List<String> partecipanti;
}
