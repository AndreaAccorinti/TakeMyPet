package com.takemypet.controller;

import com.takemypet.dto.request.CreateEventRequest;
import com.takemypet.dto.response.EventResponse;
import com.takemypet.service.EventoService;
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
@RequestMapping("/api/events")
@Tag(name = "Events", description = "Community event management")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @Operation(summary = "List all events", description = "Get all upcoming community events.")
    @ApiResponse(responseCode = "200", description = "Events listed")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventoService.getAllEvents());
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get events for user", description = "Get events organised by or participated in by the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events listed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<EventResponse>> getEventsForUser(
            @Parameter(description = "Username") @PathVariable String username) {
        return ResponseEntity.ok(eventoService.getEventsForUser(username));
    }

    @PostMapping
    @Operation(summary = "Create event", description = "Create a new community event.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Organiser not found")
    })
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoService.createEvent(request));
    }

    @PostMapping("/{eventId}/join")
    @Operation(summary = "Join event", description = "Register as a participant in an event.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joined event"),
            @ApiResponse(responseCode = "404", description = "Event or user not found")
    })
    public ResponseEntity<EventResponse> joinEvent(
            @Parameter(description = "Event ID") @PathVariable Long eventId,
            @Parameter(description = "Username") @RequestParam String username) {
        return ResponseEntity.ok(eventoService.joinEvent(eventId, username));
    }
}
