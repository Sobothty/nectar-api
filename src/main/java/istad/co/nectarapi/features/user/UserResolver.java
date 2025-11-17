package istad.co.nectarapi.features.user;

import istad.co.nectarapi.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResolver {

    private final UserRepository userRepository;

    // Get user's name from UUID or email
    public String getUserName(String identifier) {
        if (identifier == null || "SYSTEM".equals(identifier)) {
            return "SYSTEM";
        }

        // Try to find by UUID first
        return userRepository.findByUuid(identifier)
                .map(User::getName)
                .orElseGet(() -> {
                    // Try by email if UUID not found
                    return userRepository.findByEmail(identifier)
                            .map(User::getName)
                            .orElse(identifier); // Return identifier if user not found
                });
    }

    // Get user's email from UUID
    public String getUserEmail(String uuid) {
        if (uuid == null || "SYSTEM".equals(uuid)) {
            return "SYSTEM";
        }

        return userRepository.findByUuid(uuid)
                .map(User::getEmail)
                .orElse(uuid);
    }
}