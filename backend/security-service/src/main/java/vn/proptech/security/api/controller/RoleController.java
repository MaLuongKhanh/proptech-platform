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
import vn.proptech.security.domain.model.Permission;
import vn.proptech.security.domain.model.Role;
import vn.proptech.security.application.service.RoleService;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/securities/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Role Management", description = "Role and permission management endpoints")
public class RoleController {

    private final RoleService roleService;

    // Role management endpoints
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all roles", description = "Get a list of all roles (admin only)")
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
        log.info("REST request to get all roles");
        try {
            List<Role> roles = roleService.getAllRoles();
            return ApiResponse.ok(roles);
        } catch (Exception e) {
            log.error("Error getting all roles: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error retrieving roles: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get role by ID", description = "Get a role by its ID (admin only)")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable String id) {
        log.info("REST request to get role by id: {}", id);
        try {
            Role role = roleService.getRoleById(id);
            return ApiResponse.ok(role);
        } catch (Exception e) {
            log.error("Error getting role by id: {}", e.getMessage(), e);
            return ApiResponse.notFound("Role not found: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create role", description = "Create a new role (admin only)")
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role role) {
        log.info("REST request to create role: {}", role.getName());
        try {
            role.setCreatedAt(Instant.now());
            Role createdRole = roleService.createRole(role);
            return ApiResponse.created(createdRole);
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error creating role: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update role", description = "Update an existing role (admin only)")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable String id, @Valid @RequestBody Role roleDetails) {
        log.info("REST request to update role: {}", id);
        try {
            roleDetails.setUpdatedAt(Instant.now());
            Role updatedRole = roleService.updateRole(id, roleDetails);
            return ApiResponse.ok(updatedRole);
        } catch (Exception e) {
            log.error("Error updating role: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error updating role: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete role", description = "Delete a role (admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String id) {
        log.info("REST request to delete role: {}", id);
        try {
            boolean deleted = roleService.deleteRole(id);
            if (deleted) {
                return ApiResponse.noContent();
            } else {
                return ApiResponse.badRequest("Failed to delete role");
            }
        } catch (Exception e) {
            log.error("Error deleting role: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error deleting role: " + e.getMessage());
        }
    }

    // Permission management endpoints
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Add permissions to role", description = "Add permissions to a role (admin only)")
    public ResponseEntity<ApiResponse<Role>> addPermissionsToRole(@PathVariable String roleId, @RequestBody Set<String> permissionNames) {
        log.info("REST request to add permissions to role: {}", roleId);
        try {
            Role updatedRole = roleService.addPermissionsToRole(roleId, permissionNames);
            return ApiResponse.ok(updatedRole);
        } catch (Exception e) {
            log.error("Error adding permissions to role: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error adding permissions to role: " + e.getMessage());
        }
    }

    @DeleteMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Remove permissions from role", description = "Remove permissions from a role (admin only)")
    public ResponseEntity<ApiResponse<Role>> removePermissionsFromRole(@PathVariable String roleId, @RequestBody Set<String> permissionNames) {
        log.info("REST request to remove permissions from role: {}", roleId);
        try {
            Role updatedRole = roleService.removePermissionsFromRole(roleId, permissionNames);
            return ApiResponse.ok(updatedRole);
        } catch (Exception e) {
            log.error("Error removing permissions from role: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error removing permissions from role: " + e.getMessage());
        }
    }

    // Permission CRUD endpoints
    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all permissions", description = "Get a list of all permissions (admin only)")
    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissions() {
        log.info("REST request to get all permissions");
        try {
            List<Permission> permissions = roleService.getAllPermissions();
            return ApiResponse.ok(permissions);
        } catch (Exception e) {
            log.error("Error getting all permissions: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error retrieving permissions: " + e.getMessage());
        }
    }

    @GetMapping("/permissions/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get permission by ID", description = "Get a permission by its ID (admin only)")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable String id) {
        log.info("REST request to get permission by id: {}", id);
        try {
            Permission permission = roleService.getPermissionById(id);
            return ApiResponse.ok(permission);
        } catch (Exception e) {
            log.error("Error getting permission by id: {}", e.getMessage(), e);
            return ApiResponse.notFound("Permission not found: " + e.getMessage());
        }
    }

    @GetMapping("/permissions/category/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get permissions by category", description = "Get a list of permissions by category (admin only)")
    public ResponseEntity<ApiResponse<List<Permission>>> getPermissionsByCategory(@PathVariable String category) {
        log.info("REST request to get permissions by category: {}", category);
        try {
            List<Permission> permissions = roleService.getPermissionsByCategory(category);
            return ApiResponse.ok(permissions);
        } catch (Exception e) {
            log.error("Error getting permissions by category: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error retrieving permissions: " + e.getMessage());
        }
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create permission", description = "Create a new permission (admin only)")
    public ResponseEntity<ApiResponse<Permission>> createPermission(@Valid @RequestBody Permission permission) {
        log.info("REST request to create permission: {}", permission.getName());
        try {
            permission.setCreatedAt(Instant.now());
            Permission createdPermission = roleService.createPermission(permission);
            return ApiResponse.created(createdPermission);
        } catch (Exception e) {
            log.error("Error creating permission: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error creating permission: " + e.getMessage());
        }
    }

    @PutMapping("/permissions/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update permission", description = "Update an existing permission (admin only)")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@PathVariable String id, @Valid @RequestBody Permission permissionDetails) {
        log.info("REST request to update permission: {}", id);
        try {
            permissionDetails.setUpdatedAt(Instant.now());
            Permission updatedPermission = roleService.updatePermission(id, permissionDetails);
            return ApiResponse.ok(updatedPermission);
        } catch (Exception e) {
            log.error("Error updating permission: {}", e.getMessage(), e);
            return ApiResponse.badRequest("Error updating permission: " + e.getMessage());
        }
    }

    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete permission", description = "Delete a permission (admin only)")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String id) {
        log.info("REST request to delete permission: {}", id);
        try {
            boolean deleted = roleService.deletePermission(id);
            if (deleted) {
                return ApiResponse.noContent();
            } else {
                return ApiResponse.badRequest("Failed to delete permission");
            }
        } catch (Exception e) {
            log.error("Error deleting permission: {}", e.getMessage(), e);
            return ApiResponse.serverError("Error deleting permission: " + e.getMessage());
        }
    }
}