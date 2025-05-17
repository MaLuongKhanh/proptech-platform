package vn.proptech.security.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import vn.proptech.security.application.dto.input.UpdateUserRequest;
import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.application.mapper.output.GetUserResponseMapper;
import vn.proptech.security.domain.model.User;
import vn.proptech.security.domain.repository.UserRepository;
import vn.proptech.security.infrastructure.messaging.UserEventPublisher;
import vn.proptech.security.application.extension.CommonCloudinaryAttribute;
import vn.proptech.security.application.extension.CommonFileType;

import java.io.File;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;
    private final Cloudinary cloudinary;

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
        
        if (updateUserRequest.getAvatar() != null) {
            try {
                String newId = UUID.randomUUID().toString();
                String assetFolderImage = CommonCloudinaryAttribute.assetFolderSecurity;
                String publicId = User.class.getSimpleName() + "_" + newId;

                // upload feature image
                String featuredImageUrl = uploadImageToCloudinary(
                        updateUserRequest.getAvatar(),
                        assetFolderImage,
                        publicId + "_featured",
                        cloudinary
                );
                user.setAvatarUrl(featuredImageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
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
        
        String normalizedRoleName = roleName.replaceFirst("^ROLE_", "").toUpperCase();

        if (user.getRoles().contains(normalizedRoleName)) {
            return false; // Vai trò đã tồn tại, không thêm
        } else {
            user.getRoles().add(normalizedRoleName); // Thêm vai trò khác
        }
        
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
        
        String normalizedRoleName = roleName.replaceFirst("^ROLE_", "").toUpperCase();

        if (!user.getRoles().contains(normalizedRoleName)) {
            return false; // Role not assigned
        } else {
            user.getRoles().remove(normalizedRoleName);
        }
        user.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(user);
        
        // Publish user updated event
        userEventPublisher.publishUserUpdatedEvent(updatedUser);
        
        return true;
    }

    private String uploadImageToCloudinary(MultipartFile file, String assetFolder, String publicId, Cloudinary cloudinary) throws Exception {
        // Danh sách các loại file được phép
        List<String> allowedContentTypes = Arrays.asList(
                CommonFileType.JPEG,
                CommonFileType.PNG,
                CommonFileType.WEBP,
                CommonFileType.GIF
        );
        String contentType = file.getContentType();

        // Kiểm tra loại file
        if (!allowedContentTypes.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType + ". Allowed types: " + String.join(", ", allowedContentTypes));
        }

        // Lưu file tạm thời vào local
        String folderPath = Paths.get(System.getProperty("user.dir"), "upload").toString();
        File uploadDir = new File(folderPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File tempFile = new File(folderPath, file.getOriginalFilename());
        file.transferTo(tempFile);

        // Upload lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "folder", assetFolder,
                "public_id", publicId
        ));

        // Xóa file tạm thời
        tempFile.delete();

        // Trả về URL của ảnh
        return (String) uploadResult.get("secure_url");
    }
}