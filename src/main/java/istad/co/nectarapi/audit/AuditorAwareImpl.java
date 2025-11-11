package istad.co.nectarapi.audit;

import istad.co.nectarapi.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // No authentication or not authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("SYSTEM");
        }

        // Anonymous user
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM");
        }

        // JWT Authentication (Most common case - from access token)
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Get UUID from JWT claims (better than email)
            String uuid = jwt.getClaimAsString("uuid");
            if (uuid != null) {
                return Optional.of(uuid);
            }

            // Fallback to email if UUID not found
            String email = jwt.getClaimAsString("email");
            if (email != null) {
                return Optional.of(email);
            }

            // Last fallback to subject
            return Optional.of(jwt.getSubject());
        }

        // UserDetails Authentication (during login/register before JWT)
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }

        // Fallback
        return Optional.of(authentication.getName());
    }
}