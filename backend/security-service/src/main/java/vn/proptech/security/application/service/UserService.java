package vn.proptech.security.application.service;

import vn.proptech.security.application.dto.input.UpdateUserRequest;
import vn.proptech.security.application.dto.output.GetUserResponse;

import java.util.List;

public interface UserService {
    GetUserResponse getUserById(String id);
    GetUserResponse getUserByUsername(String username);
    List<GetUserResponse> getAllUsers();
    GetUserResponse updateUser(String id, UpdateUserRequest updateUserRequest);
    boolean deleteUser(String id);
    boolean disableUser(String id);
    boolean enableUser(String id);
    boolean addRoleToUser(String userId, String roleName);
    boolean removeRoleFromUser(String userId, String roleName);
}