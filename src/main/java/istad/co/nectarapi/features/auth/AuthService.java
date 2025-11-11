package istad.co.nectarapi.features.auth;

import istad.co.nectarapi.features.auth.dto.AuthResponse;
import istad.co.nectarapi.features.auth.dto.LoginRequest;
import istad.co.nectarapi.features.auth.dto.RefreshTokenRequest;
import istad.co.nectarapi.features.auth.dto.RegisterRequest;
import istad.co.nectarapi.features.user.dto.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
