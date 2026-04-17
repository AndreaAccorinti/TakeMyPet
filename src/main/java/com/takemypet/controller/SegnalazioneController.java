package com.takemypet.controller;

import com.takemypet.dto.request.SubmitReportRequest;
import com.takemypet.dto.response.ReportResponse;
import com.takemypet.service.SegnalazioneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Submit reports against users, announcements, or events")
public class SegnalazioneController {

    private final SegnalazioneService segnalazioneService;

    public SegnalazioneController(SegnalazioneService segnalazioneService) {
        this.segnalazioneService = segnalazioneService;
    }

    @PostMapping
    @Operation(summary = "Submit report", description = "Report inappropriate behaviour by a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Report submitted"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ReportResponse> submitReport(@Valid @RequestBody SubmitReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(segnalazioneService.submitReport(request));
    }
}
