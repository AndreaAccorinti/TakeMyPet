package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Pet-sitting announcement")
public class AnnouncementResponse {

    @Schema(description = "Announcement ID")
    private Long id;

    @Schema(description = "Username of the pet owner")
    private String usernameProprietario;

    @Schema(description = "Announcement description")
    private String descrizione;

    @Schema(description = "GPS latitude")
    private String latitudine;

    @Schema(description = "GPS longitude")
    private String longitudine;

    @Schema(description = "Pets included in the announcement")
    private List<PetResponse> animali;

    @Schema(description = "Pet sitters who applied")
    private List<String> petSitterUsernames;
}
