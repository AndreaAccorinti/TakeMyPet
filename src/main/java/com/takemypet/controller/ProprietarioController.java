package com.takemypet.controller;

import com.takemypet.dto.request.AddPetRequest;
import com.takemypet.dto.request.CreateAnnouncementRequest;
import com.takemypet.dto.response.AnnouncementResponse;
import com.takemypet.dto.response.PetResponse;
import com.takemypet.service.ProprietarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners/{username}")
@Tag(name = "Pet Owner", description = "Pet and announcement management for pet owners")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    // ── Pets ──────────────────────────────────────────────────────────────────

    @GetMapping("/pets")
    @Operation(summary = "List pets", description = "Get all pets belonging to the owner.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pets listed"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    public ResponseEntity<List<PetResponse>> getPets(
            @Parameter(description = "Owner username") @PathVariable String username) {
        return ResponseEntity.ok(proprietarioService.getPetsForOwner(username));
    }

    @PostMapping("/pets")
    @Operation(summary = "Add pet", description = "Add a new pet to the owner's list.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pet added"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    public ResponseEntity<PetResponse> addPet(
            @PathVariable String username,
            @Valid @RequestBody AddPetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proprietarioService.addPet(username, request));
    }

    @DeleteMapping("/pets/{petId}")
    @Operation(summary = "Delete pet", description = "Remove a pet from the owner's list.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pet deleted"),
            @ApiResponse(responseCode = "404", description = "Pet or owner not found")
    })
    public ResponseEntity<Void> deletePet(
            @PathVariable String username,
            @PathVariable Long petId) {
        proprietarioService.deletePet(username, petId);
        return ResponseEntity.noContent().build();
    }

    // ── Announcements ─────────────────────────────────────────────────────────

    @GetMapping("/announcements")
    @Operation(summary = "List announcements", description = "Get all announcements posted by the owner.")
    @ApiResponse(responseCode = "200", description = "Announcements listed")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncements(
            @PathVariable String username) {
        return ResponseEntity.ok(proprietarioService.getAnnouncementsForOwner(username));
    }

    @PostMapping("/announcements")
    @Operation(summary = "Create announcement", description = "Post a new pet-sitting announcement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Announcement created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Owner or pet not found")
    })
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @PathVariable String username,
            @Valid @RequestBody CreateAnnouncementRequest request) {
        request.setUsernameProprietario(username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proprietarioService.createAnnouncement(request));
    }

    @DeleteMapping("/announcements/{announcementId}")
    @Operation(summary = "Delete announcement", description = "Remove an announcement.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Announcement deleted"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable String username,
            @PathVariable Long announcementId) {
        proprietarioService.deleteAnnouncement(username, announcementId);
        return ResponseEntity.noContent().build();
    }
}
