package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Data for submitting a rating for a user")
public class SubmitRatingRequest {

    @NotBlank(message = "Rated user username is required")
    @Schema(description = "Username of the user being rated", example = "petsitter_luca")
    private String usernameValutato;

    @NotBlank(message = "Rater username is required")
    @Schema(description = "Username of the user submitting the rating", example = "mario_rossi")
    private String usernameValutatore;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5")
    @Schema(description = "Rating from 1.0 to 5.0 (paw scale)", example = "4.5")
    private Float numeroOrme;

    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    @Schema(description = "Optional comment", example = "Great pet sitter, very reliable!")
    private String commento;
}
