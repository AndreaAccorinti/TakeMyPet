package com.takemypet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data for adding a new pet")
public class AddPetRequest {

    @NotBlank(message = "Pet type is required")
    @Schema(description = "Type of animal", example = "Cane")
    private String tipo;

    @Schema(description = "Breed", example = "Labrador")
    private String razza;

    @NotBlank(message = "Pet name is required")
    @Schema(description = "Pet's name", example = "Rex")
    private String nome;

    @Min(value = 0, message = "Age cannot be negative")
    @Schema(description = "Age in years", example = "3")
    private Integer eta;

    @Schema(description = "Additional details", example = "Friendly and vaccinated")
    private String dettagli;

    @Schema(description = "Date of birth", example = "2021-03-10")
    private LocalDate dataDiNascita;
}
