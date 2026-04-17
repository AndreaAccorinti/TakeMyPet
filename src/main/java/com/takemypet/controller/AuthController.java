package com.takemypet.controller;

import com.takemypet.dto.request.LoginRequest;
import com.takemypet.dto.request.UnlockAccountRequest;
import com.takemypet.dto.response.UserResponse;
import com.takemypet.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login and account unlock operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username and password. Returns user profile on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Account blocked")
    })
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/unlock")
    @Operation(summary = "Unlock account", description = "Unlock a blocked account using the code sent by email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account unlocked"),
            @ApiResponse(responseCode = "400", description = "Invalid unlock code"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> unlockAccount(@Valid @RequestBody UnlockAccountRequest request) {
        return ResponseEntity.ok(authService.unlockAccount(request));
    }
}
