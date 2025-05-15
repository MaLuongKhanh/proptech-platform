package vn.proptech.security.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @Size(min = 6, max = 40)
    private String password;

    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 100)
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;
    
    private MultipartFile avatar;
}