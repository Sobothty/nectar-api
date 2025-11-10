package istad.co.nectarapi.features.auth;

import istad.co.nectarapi.domain.Role;
import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.enums.RoleName;
import istad.co.nectarapi.features.auth.dto.AuthResponse;
import istad.co.nectarapi.features.auth.dto.LoginRequest;
import istad.co.nectarapi.features.auth.dto.RefreshTokenRequest;
import istad.co.nectarapi.features.auth.dto.RegisterRequest;
import istad.co.nectarapi.features.role.RoleRepository;
import istad.co.nectarapi.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtService jwtService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Phone number already exists"
            );
        }


        // Find USER role
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Role USER not found in database"
                ));

        // Create new user
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setPhoneNumber(request.phoneNumber());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setGender(request.gender());
        user.setDob(request.dob());
        user.setIsDeleted(false);
        user.setIsBlocked(false);
        user.setRoles(List.of(userRole));

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getEmail());

        // Auto login after registration
        return login(new LoginRequest(request.email(), request.password()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        authentication = daoAuthenticationProvider.authenticate(authentication);

        log.info("User authenticated: {}", authentication.getName());

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        return new AuthResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // Validate refresh token
        Authentication authentication = new BearerTokenAuthenticationToken(request.refreshToken());
        authentication = jwtAuthenticationProvider.authenticate(authentication);

        log.info("Refresh token validated for user: {}", authentication.getName());

        // Get user from database to get latest authorities
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        // Create authentication with latest authorities
        String scope = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.joining(" "));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                user.getRoles()
        );

        // Generate new access token
        String accessToken = jwtService.generateAccessToken(newAuth);

        return new AuthResponse("Bearer", accessToken, request.refreshToken());
    }
}