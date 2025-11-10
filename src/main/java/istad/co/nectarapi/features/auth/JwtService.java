package istad.co.nectarapi.features.auth;

import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtEncoder refreshJwtEncoder;
    private final UserRepository userRepository;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Qualifier("refreshJwtEncoder") JwtEncoder refreshJwtEncoder,
            UserRepository userRepository
    ) {
        this.jwtEncoder = jwtEncoder;
        this.refreshJwtEncoder = refreshJwtEncoder;
        this.userRepository = userRepository;
    }

    public String generateAccessToken(Authentication authentication) {
        Instant now = Instant.now();

        // Get user from database to add more info
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        // Extract roles
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Extract role names (without ROLE_ prefix)
        String roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(","));

        // Build JWT with custom claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS)) // 1 hour
                .subject(authentication.getName()) // email

                // Custom Claims - Add more user info
                .claim("scope", scope) // "ROLE_ADMIN ROLE_USER"
                .claim("uuid", user.getUuid())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("roles", roles) // "ADMIN,USER"
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        // Get user info for refresh token
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS)) // 30 days
                .subject(authentication.getName())

                //  Add minimal info to refresh token
                .claim("tokenType", "refresh")
                .claim("uuid", user.getUuid())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("phoneNumber", user.getPhoneNumber())
                .build();

        return refreshJwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}