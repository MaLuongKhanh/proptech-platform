package vn.proptech.security.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.proptech.security.api.common.ApiResponse;
import vn.proptech.security.application.dto.input.UpdateUserRequest;
import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.application.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/securities/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all users", description = "Get a list of all users (admin only)")
    public ResponseEntity<ApiResponse<List<GetUserResponse>>> getAllUsers() {
        log.info("REST request to get all users");
        try {
            List<GetUserResponse> users = userService.getAllUsers();
            return ApiResponse.ok(users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error retrieving users: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get a user by their ID")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUserById(@PathVariable String id) {
        log.info("REST request to get user by id: {}", id);
        try {
            GetUserResponse user = userService.getUserById(id);
            return ApiResponse.ok(user);
        } catch (Exception e) {
            log.error("Error getting user by id: {}", e.getMessage(), e);
            return ApiResponse.notFound("User not found: " + e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Get a user by their username")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUserByUsername(@PathVariable String username) {
        log.info("REST request to get user by username: {}", username);
        try {
            GetUserResponse user = userService.getUserByUsername(username);
            return ApiResponse.ok(user);
        } catch (Exception e) {
            log.error("Error getting user by username: {}", e.getMessage(), e);
            return ApiResponse.notFound("User not found: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update a user's information")
    public ResponseEntity<ApiResponse<GetUserResponse>> updateUser(@PathVariable String id, 
                                                               @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("REST request to update user: {}", id);
        try {
            GetUserResponse updatedUser = userService.updateUser(id, updateUserRequest);
            return ApiResponse.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete user", description = "Delete a user (admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        log.info("REST request to delete user: {}", id);
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ApiResponse.noContent();
            } else {
                return ApiResponse.badRequest("Failed to delete user");
            }
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error deleting user: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Disable user", description = "Disable a user (admin only)")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable String id) {
        log.info("REST request to disable user: {}", id);
        try {
            boolean disabled = userService.disableUser(id);
            if (disabled) {
                return ApiResponse.ok(null);
            } else {
                return ApiResponse.badRequest("Failed to disable user");
            }
        } catch (Exception e) {
            log.error("Error disabling user: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error disabling user: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Enable user", description = "Enable a user (admin only)")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable String id) {
        log.info("REST request to enable user: {}", id);
        try {
            boolean enabled = userService.enableUser(id);
            if (enabled) {
                return ApiResponse.ok(null);
            } else {
                return ApiResponse.badRequest("Failed to enable user");
            }
        } catch (Exception e) {
            log.error("Error enabling user: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error enabling user: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Add role to user", description = "Add a role to a user (admin only)")
    public ResponseEntity<ApiResponse<Void>> addRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        log.info("REST request to add role {} to user: {}", roleName, userId);
        try {
            boolean added = userService.addRoleToUser(userId, roleName);
            if (added) {
                return ApiResponse.ok(null);
            } else {
                return ApiResponse.badRequest("Role already assigned to user");
            }
        } catch (Exception e) {
            log.error("Error adding role to user: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error adding role to user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Remove role from user", description = "Remove a role from a user (admin only)")
    public ResponseEntity<ApiResponse<Void>> removeRoleFromUser(@PathVariable String userId, @PathVariable String roleName) {
        log.info("REST request to remove role {} from user: {}", roleName, userId);
        try {
            boolean removed = userService.removeRoleFromUser(userId, roleName);
            if (removed) {
                return ApiResponse.ok(null);
            } else {
                return ApiResponse.badRequest("Role not assigned to user");
            }
        } catch (Exception e) {
            log.error("Error removing role from user: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error removing role from user: " + e.getMessage());
        }
    }
}