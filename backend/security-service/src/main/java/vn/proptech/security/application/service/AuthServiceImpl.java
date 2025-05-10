package vn.proptech.security.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.proptech.security.application.dto.input.LoginRequest;
import vn.proptech.security.application.dto.input.RegisterRequest;
import vn.proptech.security.application.dto.input.ResetPasswordRequest;
import vn.proptech.security.application.dto.output.GetJwtResponse;
import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.application.mapper.output.GetJwtResponseMapper;
import vn.proptech.security.application.mapper.output.GetUserResponseMapper;
import vn.proptech.security.domain.model.User;
import vn.proptech.security.domain.repository.UserRepository;
import vn.proptech.security.exception.GlobalExceptionHandler.ResourceNotFoundException;
import vn.proptech.security.infrastructure.messaging.UserEventPublisher;
import vn.proptech.security.infrastructure.security.JwtTokenProvider;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // This would typically be stored in a database in a production environment
    private final Map<String, PasswordResetToken> passwordResetTokens = new ConcurrentHashMap<>();
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserEventPublisher userEventPublisher;

    @Override
    public GetJwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + userDetails.getUsername()));
        
        // Update last login time
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);
        
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return GetJwtResponseMapper.toGetJwtResponse(
                accessToken, 
                refreshToken, 
                userDetails,
                user.getId(),
                user.getAvatarUrl(),
                user.getFullName()
        );
    }

    @Override
    public GetUserResponse registerUser(RegisterRequest registerRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setCreatedAt(Instant.now());
        
        // Assign default ROLE_USER role
        HashSet<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        
        // Publish user created event
        userEventPublisher.publishUserCreatedEvent(savedUser);

        return GetUserResponseMapper.toGetUserResponse(savedUser);
    }

    @Override
    public GetJwtResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Create authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null
        );

        // Generate new tokens
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authentication.getAuthorities()
        );

        return GetJwtResponseMapper.toGetJwtResponse(
                newAccessToken,
                newRefreshToken,
                userDetails,
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
    }
    
    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Generate a secure random token
        String token = generateSecureToken();
        
        // Store the token with expiration time (1 hour from now)
        passwordResetTokens.put(token, new PasswordResetToken(user.getId(), Instant.now().plusSeconds(3600)));
        
        // In a real application, send the reset link to the user's email
        // emailService.sendPasswordResetEmail(user.getEmail(), token);
        
        log.info("Password reset requested for user: {}, token: {}", user.getUsername(), token);
    }
    
    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        
        String token = resetPasswordRequest.getToken();
        PasswordResetToken passwordResetToken = passwordResetTokens.get(token);
        
        if (passwordResetToken == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        
        if (passwordResetToken.getExpirationTime().isBefore(Instant.now())) {
            passwordResetTokens.remove(token);
            throw new IllegalArgumentException("Token has expired");
        }
        
        User user = userRepository.findById(passwordResetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Update the password
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        
        // Remove the used token
        passwordResetTokens.remove(token);
        
        log.info("Password has been reset for user: {}", user.getUsername());
    }
    
    @Override
    public boolean validateResetToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        
        PasswordResetToken passwordResetToken = passwordResetTokens.get(token);
        
        if (passwordResetToken == null) {
            return false;
        }
        
        if (passwordResetToken.getExpirationTime().isBefore(Instant.now())) {
            passwordResetTokens.remove(token);
            return false;
        }
        
        return true;
    }
    
    // Helper methods
    private String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    // Inner class to store token data
    private static class PasswordResetToken {
        private final String userId;
        private final Instant expirationTime;
        
        public PasswordResetToken(String userId, Instant expirationTime) {
            this.userId = userId;
            this.expirationTime = expirationTime;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public Instant getExpirationTime() {
            return expirationTime;
        }
    }
}