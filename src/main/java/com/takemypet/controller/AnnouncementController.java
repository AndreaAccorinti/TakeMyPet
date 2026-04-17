package com.takemypet.controller;

import com.takemypet.dto.response.AnnouncementResponse;
import com.takemypet.service.PetSitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@Tag(name = "Announcements", description = "Browse announcements and apply as pet sitter")
public class AnnouncementController {

    private final PetSitterService petSitterService;

    public AnnouncementController(PetSitterService petSitterService) {
        this.petSitterService = petSitterService;
    }

    @GetMapping
    @Operation(summary = "List all announcements", description = "Get all open pet-sitting announcements.")
    @ApiResponse(responseCode = "200", description = "Announcements listed")
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {
        return ResponseEntity.ok(petSitterService.getAllAnnouncements());
    }

    @PostMapping("/{announcementId}/apply")
    @Operation(summary = "Apply to announcement", description = "A pet sitter applies to an open announcement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application submitted"),
            @ApiResponse(responseCode = "404", description = "Announcement or pet sitter not found")
    })
    public ResponseEntity<AnnouncementResponse> applyToAnnouncement(
            @Parameter(description = "Announcement ID") @PathVariable Long announcementId,
            @Parameter(description = "Pet sitter username") @RequestParam String petSitterUsername) {
        return ResponseEntity.ok(petSitterService.applyToAnnouncement(announcementId, petSitterUsername));
    }
}
