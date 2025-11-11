package istad.co.nectarapi.features.user;

import istad.co.nectarapi.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserResolver {
    private final UserRepository userRepository;

    public UserResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserName(String uuid) {
        if (uuid == null) return null;
        return userRepository.findByUuid(uuid)
                .map(User::getName)
                .orElse("SYSTEM");
    }
}