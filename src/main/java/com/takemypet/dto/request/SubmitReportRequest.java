package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data for submitting a report against a user")
public class SubmitReportRequest {

    @NotBlank(message = "Reported user username is required")
    @Schema(description = "Username of the user being reported", example = "bad_user")
    private String usernameSegnatalo;

    @NotBlank(message = "Reporter username is required")
    @Schema(description = "Username of the user submitting the report", example = "mario_rossi")
    private String usernameSegnalatore;

    @NotBlank(message = "Description is required")
    @Schema(description = "Reason for the report", example = "Inappropriate behavior")
    private String descrizione;

    @Schema(description = "ID of the related announcement (optional)")
    private Long annuncioId;

    @Schema(description = "ID of the related event (optional)")
    private Long eventoId;
}
