package com.takemypet.controller;

import com.takemypet.dto.response.RegistrationStatResponse;
import com.takemypet.dto.response.ReportResponse;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Administrative operations: user moderation and statistics")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── Users ─────────────────────────────────────────────────────────────────

    @GetMapping("/users")
    @Operation(summary = "List all users", description = "Get all non-admin users.")
    @ApiResponse(responseCode = "200", description = "Users listed")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllNonAdminUsers());
    }

    @PutMapping("/users/{username}/block")
    @Operation(summary = "Block user", description = "Block a user account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User blocked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> blockUser(
            @Parameter(description = "Username") @PathVariable String username) {
        adminService.blockUser(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/unblock")
    @Operation(summary = "Unblock user", description = "Unblock a blocked user account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User unblocked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> unblockUser(@PathVariable String username) {
        adminService.unblockUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{username}")
    @Operation(summary = "Delete user", description = "Permanently delete a user account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    // ── Reports ───────────────────────────────────────────────────────────────

    @GetMapping("/reports")
    @Operation(summary = "List all reports", description = "Get all user reports.")
    @ApiResponse(responseCode = "200", description = "Reports listed")
    public ResponseEntity<List<ReportResponse>> getAllReports() {
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @DeleteMapping("/reports/{reportId}")
    @Operation(summary = "Delete report", description = "Delete a report after review.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Report deleted"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        adminService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }

    // ── Content moderation ────────────────────────────────────────────────────

    @DeleteMapping("/announcements/{announcementId}")
    @Operation(summary = "Delete announcement", description = "Remove an announcement for policy violations.")
    @ApiResponse(responseCode = "204", description = "Announcement deleted")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long announcementId) {
        adminService.deleteAnnouncement(announcementId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/events/{eventId}")
    @Operation(summary = "Delete event", description = "Remove an event for policy violations.")
    @ApiResponse(responseCode = "204", description = "Event deleted")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        adminService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ratings/{ratingId}")
    @Operation(summary = "Delete rating", description = "Remove a rating for policy violations.")
    @ApiResponse(responseCode = "204", description = "Rating deleted")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        adminService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }

    // ── Statistics ────────────────────────────────────────────────────────────

    @GetMapping("/stats/registrations")
    @Operation(summary = "Registration statistics", description = "Count of new user registrations grouped by day.")
    @ApiResponse(responseCode = "200", description = "Statistics returned")
    public ResponseEntity<List<RegistrationStatResponse>> getRegistrationStats() {
        return ResponseEntity.ok(adminService.getRegistrationStatsByDay());
    }
}
