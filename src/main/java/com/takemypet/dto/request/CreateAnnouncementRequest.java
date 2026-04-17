package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Data for creating a pet-sitting announcement")
public class CreateAnnouncementRequest {

    @NotBlank(message = "Owner username is required")
    @Schema(description = "Username of the pet owner posting the announcement", example = "mario_rossi")
    private String usernameProprietario;

    @NotBlank(message = "Description is required")
    @Schema(description = "Announcement description", example = "Need a sitter for my dog this weekend")
    private String descrizione;

    @Schema(description = "GPS latitude", example = "41.9028")
    private String latitudine;

    @Schema(description = "GPS longitude", example = "12.4964")
    private String longitudine;

    @NotEmpty(message = "At least one pet must be included")
    @Schema(description = "IDs of the pets included in this announcement")
    private List<Long> animaleIds;
}
