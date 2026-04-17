package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User report")
public class ReportResponse {

    @Schema(description = "Report ID")
    private Long id;

    @Schema(description = "Username of the reported user")
    private String usernameSegnalato;

    @Schema(description = "Username of the reporter")
    private String usernameSegnalatore;

    @Schema(description = "Reason for the report")
    private String descrizione;

    @Schema(description = "Related announcement ID (if any)")
    private Long annuncioId;

    @Schema(description = "Related event ID (if any)")
    private Long eventoId;
}
