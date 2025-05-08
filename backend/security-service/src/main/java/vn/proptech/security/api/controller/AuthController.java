package vn.proptech.security.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.proptech.security.api.common.ApiResponse;
import vn.proptech.security.application.dto.input.LoginRequest;
import vn.proptech.security.application.dto.input.RegisterRequest;
import vn.proptech.security.application.dto.input.ResetPasswordRequest;
import vn.proptech.security.application.dto.output.GetJwtResponse;
import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.application.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/securities/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Login with username and password to receive JWT token")
    public ResponseEntity<ApiResponse<GetJwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("REST request to login user: {}", loginRequest.getUsername());
        try {
            GetJwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ApiResponse.ok(jwtResponse);
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage(), e);
            return ApiResponse.unauthorized("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user with basic details")
    public ResponseEntity<ApiResponse<GetUserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("REST request to register user: {}", registerRequest.getUsername());
        try {
            GetUserResponse userResponse = authService.registerUser(registerRequest);
            return ApiResponse.created(userResponse);
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<ApiResponse<GetJwtResponse>> refreshToken(@RequestParam String refreshToken) {
        log.info("REST request to refresh token");
        try {
            GetJwtResponse jwtResponse = authService.refreshToken(refreshToken);
            return ApiResponse.ok(jwtResponse);
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage(), e);
            return ApiResponse.unauthorized("Token refresh failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Request a password reset email")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        log.info("REST request for password reset for email: {}", email);
        try {
            authService.requestPasswordReset(email);
            return ApiResponse.ok(null);
        } catch (Exception e) {
            log.error("Error requesting password reset: {}", e.getMessage(), e);
            // Still return OK to prevent email enumeration attacks
            return ApiResponse.ok(null);
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using token")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("REST request to reset password");
        try {
            authService.resetPassword(resetPasswordRequest);
            return ApiResponse.ok(null);
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Password reset failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/validate-token")
    @Operation(summary = "Validate token", description = "Validate a reset password token")
    public ResponseEntity<ApiResponse<Boolean>> validateResetToken(@RequestParam String token) {
        log.info("REST request to validate reset token");
        try {
            boolean isValid = authService.validateResetToken(token);
            return ApiResponse.ok(isValid);
        } catch (Exception e) {
            log.error("Error validating reset token: {}", e.getMessage(), e);
            return ApiResponse.ok(false);
        }
    }
}