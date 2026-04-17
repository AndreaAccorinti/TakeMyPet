package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Number of user registrations on a given day")
public class RegistrationStatResponse {

    @Schema(description = "Registration date", example = "2024-06-01")
    private LocalDate date;

    @Schema(description = "Number of users registered on that day", example = "5")
    private long count;
}
