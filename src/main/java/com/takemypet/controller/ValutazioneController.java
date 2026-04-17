package com.takemypet.controller;

import com.takemypet.dto.request.SubmitRatingRequest;
import com.takemypet.dto.response.RatingResponse;
import com.takemypet.service.ValutazioneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Ratings", description = "Rate users after a pet-sitting engagement")
public class ValutazioneController {

    private final ValutazioneService valutazioneService;

    public ValutazioneController(ValutazioneService valutazioneService) {
        this.valutazioneService = valutazioneService;
    }

    @PostMapping
    @Operation(summary = "Submit rating", description = "Rate a pet sitter or pet owner after an engagement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rating submitted"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<RatingResponse> submitRating(@Valid @RequestBody SubmitRatingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valutazioneService.submitRating(request));
    }
}
