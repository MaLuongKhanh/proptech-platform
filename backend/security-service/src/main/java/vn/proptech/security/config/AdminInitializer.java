package vn.proptech.security.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.proptech.security.domain.model.Permission;
import vn.proptech.security.domain.model.Role;
import vn.proptech.security.domain.model.User;
import vn.proptech.security.domain.repository.PermissionRepository;
import vn.proptech.security.domain.repository.RoleRepository;
import vn.proptech.security.domain.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initializePermissions();
        initializeRoles();
        initializeAdminUser();
    }

    private void initializePermissions() {
        if (permissionRepository.count() == 0) {
            log.info("Khởi tạo các permission mặc định");
            
            List<Permission> permissions = Arrays.asList(
                createPermission("user:read", "Xem thông tin người dùng"),
                createPermission("user:create", "Tạo người dùng mới"),
                createPermission("user:update", "Cập nhật thông tin người dùng"),
                createPermission("user:delete", "Xóa người dùng"),
                
                createPermission("role:read", "Xem danh sách vai trò"),
                createPermission("role:create", "Tạo vai trò mới"),
                createPermission("role:update", "Cập nhật vai trò"),
                createPermission("role:delete", "Xóa vai trò"),
                
                createPermission("listing:read", "Xem danh sách bất động sản"),
                createPermission("listing:create", "Đăng tin bất động sản"),
                createPermission("listing:update", "Cập nhật tin bất động sản"),
                createPermission("listing:delete", "Xóa tin bất động sản"),
                
                createPermission("sale:read", "Xem giao dịch mua bán"),
                createPermission("sale:create", "Tạo giao dịch mua bán"),
                createPermission("sale:update", "Cập nhật giao dịch mua bán"),
                createPermission("sale:delete", "Xóa giao dịch mua bán"),
                
                createPermission("rental:read", "Xem giao dịch cho thuê"),
                createPermission("rental:create", "Tạo giao dịch cho thuê"),
                createPermission("rental:update", "Cập nhật giao dịch cho thuê"),
                createPermission("rental:delete", "Xóa giao dịch cho thuê"),
                
                createPermission("payment:read", "Xem giao dịch thanh toán"),
                createPermission("payment:create", "Tạo giao dịch thanh toán"),
                createPermission("payment:update", "Cập nhật giao dịch thanh toán"),
                createPermission("payment:delete", "Xóa giao dịch thanh toán")
            );
            
            permissionRepository.saveAll(permissions);
            log.info("Đã khởi tạo {} permission mặc định", permissions.size());
        }
    }
    
    private Permission createPermission(String name, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        return permission;
    }
    
    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Khởi tạo các vai trò mặc định");
            
            // Tạo vai trò ADMIN (có tất cả quyền)
            Set<String> adminPermissions = permissionRepository.findAll().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
            Role adminRole = createRole("ROLE_ADMIN", "Quản trị viên hệ thống", adminPermissions);
            
            // Tạo vai trò USER (chỉ có quyền đọc)
            Set<String> userPermissions = permissionRepository.findAll().stream()
                .filter(p -> p.getName().endsWith(":read"))
                .map(Permission::getName)
                .collect(Collectors.toSet());
            Role userRole = createRole("ROLE_USER", "Người dùng thông thường", userPermissions);
            
            // Tạo vai trò MANAGER (có quyền đọc, tạo, cập nhật nhưng không xóa)
            Set<String> managerPermissions = permissionRepository.findAll().stream()
                .filter(p -> !p.getName().endsWith(":delete"))
                .map(Permission::getName)
                .collect(Collectors.toSet());
            Role managerRole = createRole("ROLE_MANAGER", "Quản lý", managerPermissions);
            
            // Tạo vai trò AGENT (chỉ có quyền về listing)
            Set<String> agentPermissions = permissionRepository.findAll().stream()
                .filter(p -> p.getName().startsWith("listing:") || p.getName().startsWith("user:read"))
                .map(Permission::getName)
                .collect(Collectors.toSet());
            Role agentRole = createRole("ROLE_AGENT", "Môi giới bất động sản", agentPermissions);
            
            List<Role> roles = Arrays.asList(adminRole, userRole, managerRole, agentRole);
            roleRepository.saveAll(roles);
            log.info("Đã khởi tạo {} vai trò mặc định", roles.size());
        }
    }
    
    private Role createRole(String name, String description, Set<String> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setPermissions(permissions);
        return role;
    }

    private void initializeAdminUser() {
        // Kiểm tra nếu hệ thống chưa có user nào
        if (userRepository.count() == 0) {
            log.info("Chưa có user nào, tạo tài khoản admin mặc định");
            // Tạo user admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setEmail("admin@proptech.vn");
            adminUser.setFullName("Administrator");
            adminUser.setPhoneNumber("0000000000");
            adminUser.setEnabled(true);
            adminUser.setRoles(Set.of("ADMIN"));
            
            userRepository.save(adminUser);
            log.info("Đã tạo tài khoản admin mặc định (username: admin, password: admin)");
        } else {
            log.info("Đã có user trong hệ thống, không tạo admin mặc định");
        }
    }
}