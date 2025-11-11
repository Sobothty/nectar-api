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

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("SYSTEM");
        }

        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            return Optional.of(subject);
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            String username = userDetails.getUsername();
            return Optional.of(username);
        }

        String name = authentication.getName();
        return Optional.of(name);
    }
}