package vn.proptech.security.application.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetJwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String id;
    private String username;
    private String email;
    private String fullName;
    private List<String> roles;
}