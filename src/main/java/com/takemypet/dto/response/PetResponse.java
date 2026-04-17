package com.takemypet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Pet information")
public class PetResponse {

    @Schema(description = "Pet ID")
    private Long id;

    @Schema(description = "Type of animal", example = "Cane")
    private String tipo;

    @Schema(description = "Breed", example = "Labrador")
    private String razza;

    @Schema(description = "Pet's name", example = "Rex")
    private String nome;

    @Schema(description = "Age in years", example = "3")
    private Integer eta;

    @Schema(description = "Additional details")
    private String dettagli;

    @Schema(description = "Date of birth")
    private LocalDate dataDiNascita;

    @Schema(description = "URL of the pet's image")
    private String immagineUrl;
}
