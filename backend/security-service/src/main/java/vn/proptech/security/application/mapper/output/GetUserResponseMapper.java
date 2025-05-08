package vn.proptech.security.application.mapper.output;

import vn.proptech.security.application.dto.output.GetUserResponse;
import vn.proptech.security.domain.model.User;

public class GetUserResponseMapper {
    
    public static GetUserResponse toGetUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return GetUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}