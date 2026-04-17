package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data required to unlock a blocked account")
public class UnlockAccountRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username of the blocked account", example = "mario_rossi")
    private String username;

    @NotBlank(message = "Unlock code is required")
    @Schema(description = "Unlock code received by email", example = "TMP-34821")
    private String codiceSblocco;
}
