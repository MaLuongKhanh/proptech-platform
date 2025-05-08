package vn.proptech.security.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import vn.proptech.security.domain.model.Permission;
import vn.proptech.security.domain.model.Role;
import vn.proptech.security.domain.repository.PermissionRepository;
import vn.proptech.security.domain.repository.RoleRepository;
import vn.proptech.security.exception.GlobalExceptionHandler.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    @Override
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role with name " + role.getName() + " already exists");
        }
        
        // Set ID if not provided
        if (role.getId() == null || role.getId().isEmpty()) {
            role.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps
        if (role.getCreatedAt() == null) {
            role.setCreatedAt(Instant.now());
        }
        
        // Validate permissions
        validatePermissions(role.getPermissions());
        
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(String id, Role roleDetails) {
        Role existingRole = getRoleById(id);
        
        // Check if name is being changed and if it's already in use
        if (!existingRole.getName().equals(roleDetails.getName()) && 
            roleRepository.existsByName(roleDetails.getName())) {
            throw new IllegalArgumentException("Role with name " + roleDetails.getName() + " already exists");
        }
        
        // Update fields
        existingRole.setName(roleDetails.getName());
        existingRole.setDescription(roleDetails.getDescription());
        existingRole.setActive(roleDetails.isActive());
        
        // Validate and update permissions if provided
        if (roleDetails.getPermissions() != null && !roleDetails.getPermissions().isEmpty()) {
            validatePermissions(roleDetails.getPermissions());
            existingRole.setPermissions(roleDetails.getPermissions());
        }
        
        // Update timestamp
        existingRole.setUpdatedAt(Instant.now());
        
        return roleRepository.save(existingRole);
    }

    @Override
    public boolean deleteRole(String id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
        return true;
    }

    @Override
    public Role addPermissionsToRole(String roleId, Set<String> permissionNames) {
        Role role = getRoleById(roleId);
        
        // Validate permissions
        validatePermissions(permissionNames);
        
        // Add permissions to role
        role.getPermissions().addAll(permissionNames);
        role.setUpdatedAt(Instant.now());
        
        return roleRepository.save(role);
    }

    @Override
    public Role removePermissionsFromRole(String roleId, Set<String> permissionNames) {
        Role role = getRoleById(roleId);
        
        // Remove permissions from role
        role.getPermissions().removeAll(permissionNames);
        role.setUpdatedAt(Instant.now());
        
        return roleRepository.save(role);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
    }

    @Override
    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + name));
    }

    @Override
    public List<Permission> getPermissionsByCategory(String category) {
        return permissionRepository.findByCategory(category);
    }

    @Override
    public Permission createPermission(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("Permission with name " + permission.getName() + " already exists");
        }
        
        // Set ID if not provided
        if (permission.getId() == null || permission.getId().isEmpty()) {
            permission.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps
        if (permission.getCreatedAt() == null) {
            permission.setCreatedAt(Instant.now());
        }
        
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(String id, Permission permissionDetails) {
        Permission existingPermission = getPermissionById(id);
        
        // Check if name is being changed and if it's already in use
        if (!existingPermission.getName().equals(permissionDetails.getName()) && 
            permissionRepository.existsByName(permissionDetails.getName())) {
            throw new IllegalArgumentException("Permission with name " + permissionDetails.getName() + " already exists");
        }
        
        // Update fields
        existingPermission.setName(permissionDetails.getName());
        existingPermission.setDescription(permissionDetails.getDescription());
        existingPermission.setCategory(permissionDetails.getCategory());
        existingPermission.setActive(permissionDetails.isActive());
        
        // Update timestamp
        existingPermission.setUpdatedAt(Instant.now());
        
        return permissionRepository.save(existingPermission);
    }

    @Override
    public boolean deletePermission(String id) {
        Permission permission = getPermissionById(id);
        
        // Check if permission is used by any role
        List<Role> rolesWithPermission = roleRepository.findAll().stream()
                .filter(role -> role.getPermissions().contains(permission.getName()))
                .collect(Collectors.toList());
        
        if (!rolesWithPermission.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete permission as it is used by " + 
                    rolesWithPermission.size() + " role(s). Remove it from all roles first.");
        }
        
        permissionRepository.delete(permission);
        return true;
    }
    
    // Helper methods
    private void validatePermissions(Set<String> permissionNames) {
        for (String permissionName : permissionNames) {
            if (!permissionRepository.existsByName(permissionName)) {
                throw new ResourceNotFoundException("Permission not found with name: " + permissionName);
            }
        }
    }
}