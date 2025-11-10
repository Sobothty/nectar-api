package istad.co.nectarapi.features.user;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.domain.Role;
import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.enums.RoleName;
import istad.co.nectarapi.features.role.RoleRepository;
import istad.co.nectarapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public BasedMessage assignAdminRole(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ADMIN);

        if (hasAdminRole) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User already has ADMIN role"
            );
        }
        // Add ADMIN role (keep existing roles)
        List<Role> roles = new ArrayList<>(user.getRoles());
        roles.add(adminRole);
        user.setRoles(roles);

        userRepository.save(user);

        return new BasedMessage("Admin role assigned successfully");
    }

    @Override
    public BasedMessage removeAdminRole(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ADMIN);

        if (!hasAdminRole) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User does not have ADMIN role"
            );
        }

        if (user.getRoles().size() == 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot remove ADMIN role: User must have at least one role. User only has ADMIN role."
            );
        }
        // Remove ADMIN role but keep USER role
        List<Role> roles = user.getRoles().stream()
                .filter(role -> role.getName() != RoleName.ADMIN)
                .toList();

        user.setRoles(roles);
        userRepository.save(user);
        return new BasedMessage("Admin role removed successfully");
    }
}
