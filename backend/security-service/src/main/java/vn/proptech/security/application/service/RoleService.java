package vn.proptech.security.application.service;

import vn.proptech.security.domain.model.Role;
import vn.proptech.security.domain.model.Permission;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(String id);
    Role getRoleByName(String name);
    Role createRole(Role role);
    Role updateRole(String id, Role roleDetails);
    boolean deleteRole(String id);
    
    // Permission management within roles
    Role addPermissionsToRole(String roleId, Set<String> permissionNames);
    Role removePermissionsFromRole(String roleId, Set<String> permissionNames);
    
    // Permission operations
    List<Permission> getAllPermissions();
    Permission getPermissionById(String id);
    Permission getPermissionByName(String name);
    List<Permission> getPermissionsByCategory(String category);
    Permission createPermission(Permission permission);
    Permission updatePermission(String id, Permission permissionDetails);
    boolean deletePermission(String id);
}