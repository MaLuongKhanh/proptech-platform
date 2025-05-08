package vn.proptech.security.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.proptech.security.application.dto.input.UpdateUserRequest;
import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.application.mapper.output.GetUserResponseMapper;
import vn.proptech.security.domain.model.User;
import vn.proptech.security.domain.repository.UserRepository;
import vn.proptech.security.infrastructure.messaging.UserEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    @Override
    public GetUserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return GetUserResponseMapper.toGetUserResponse(user);
    }

    @Override
    public GetUserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return GetUserResponseMapper.toGetUserResponse(user);
    }

    @Override
    public List<GetUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(GetUserResponseMapper::toGetUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GetUserResponse updateUser(String id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().isEmpty()) {
            // Check if email exists for another user
            userRepository.findByEmail(updateUserRequest.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new RuntimeException("Email is already in use!");
                        }
                    });
            user.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getFullName() != null) {
            user.setFullName(updateUserRequest.getFullName());
        }

        if (updateUserRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserRequest.getPhoneNumber());
        }
        
        if (updateUserRequest.getAvatarUrl() != null) {
            user.setAvatarUrl(updateUserRequest.getAvatarUrl());
        }

        user.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(updatedUser);
        
        return GetUserResponseMapper.toGetUserResponse(updatedUser);
    }

    @Override
    public boolean deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Soft delete by disabling the user
        user.setEnabled(false);
        user.setUpdatedAt(Instant.now());
        User deletedUser = userRepository.save(user);
        
        // Publish user deleted event
        userEventPublisher.publishUserDeletedEvent(deletedUser);
        
        return true;
    }

    @Override
    public boolean disableUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setEnabled(false);
        user.setUpdatedAt(Instant.now());
        User disabledUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(disabledUser);
        
        return true;
    }

    @Override
    public boolean enableUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setEnabled(true);
        user.setUpdatedAt(Instant.now());
        User enabledUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(enabledUser);
        
        return true;
    }

    @Override
    public boolean addRoleToUser(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if (user.getRoles().contains(roleName)) {
            return false; // Role already assigned
        }
        
        user.getRoles().add(roleName);
        user.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(updatedUser);
        
        return true;
    }

    @Override
    public boolean removeRoleFromUser(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if (!user.getRoles().contains(roleName)) {
            return false; // Role not assigned
        }
        
        user.getRoles().remove(roleName);
        user.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(updatedUser);
        
        return true;
    }
}