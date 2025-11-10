package istad.co.nectarapi.features.auth.dto;

public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {}