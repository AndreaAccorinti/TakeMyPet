package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User rating")
public class RatingResponse {

    @Schema(description = "Rating ID")
    private Long id;

    @Schema(description = "Username of the rater")
    private String usernameValutatore;

    @Schema(description = "Username of the rated user")
    private String usernameValutato;

    @Schema(description = "Rating from 1.0 to 5.0", example = "4.5")
    private float numeroOrme;

    @Schema(description = "Optional comment")
    private String commento;
}
