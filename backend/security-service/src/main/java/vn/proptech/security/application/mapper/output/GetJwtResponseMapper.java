package vn.proptech.security.application.mapper.output;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.proptech.security.application.dto.output.GetJwtResponse;

import java.util.List;
import java.util.stream.Collectors;

public class GetJwtResponseMapper {
    
    public static GetJwtResponse toGetJwtResponse(String accessToken, String refreshToken, 
                                                  UserDetails userDetails, String id, String avatarUrl, String fullName) {
        if (userDetails == null) {
            return null;
        }
        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        return GetJwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(id)
                .fullName(fullName)
                .avatarUrl(avatarUrl)
                .roles(roles)
                .build();
    }
}