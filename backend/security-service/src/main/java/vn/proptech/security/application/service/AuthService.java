package vn.proptech.security.application.service;

import vn.proptech.security.application.dto.input.LoginRequest;
import vn.proptech.security.application.dto.input.RegisterRequest;
import vn.proptech.security.application.dto.input.ResetPasswordRequest;
import vn.proptech.security.application.dto.output.GetJwtResponse;
import vn.proptech.security.application.dto.output.GetUserResponse;

public interface AuthService {
    GetJwtResponse authenticateUser(LoginRequest loginRequest);
    GetUserResponse registerUser(RegisterRequest registerRequest);
    GetJwtResponse refreshToken(String refreshToken);
    
    void requestPasswordReset(String email);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    boolean validateResetToken(String token);
}