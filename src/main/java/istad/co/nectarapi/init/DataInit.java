package istad.co.nectarapi.init;

import istad.co.nectarapi.domain.Role;
import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.enums.RoleName;
import istad.co.nectarapi.features.role.RoleRepository;
import istad.co.nectarapi.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initRoles();  //  Initialize roles FIRST
        initAdmin();  //  Then create admin user
    }

    private void initRoles() {
        // Check if roles already exist
        if (roleRepository.count() > 0) {
            log.info("Roles already initialized");
            return;
        }

        // Create USER role
        Role userRole = new Role();
        userRole.setName(RoleName.USER);
        roleRepository.save(userRole);

        // Create ADMIN role
        Role adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);
        roleRepository.save(adminRole);

    }

    private void initAdmin() {
        // Check if admin already exists
        if (userRepository.existsByEmail("admin@nectar.com")) {
            log.info("Admin user already exists");
            return;
        }

        // Get ADMIN role (will exist because initRoles() runs first)
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        // Create default admin user
        User admin = new User();
        admin.setUuid(UUID.randomUUID().toString());
        admin.setPhoneNumber("017349304");
        admin.setEmail("admin@nectar.com");
        admin.setPassword(passwordEncoder.encode("darasmos123"));
        admin.setName("System Administrator");
        admin.setGender("OTHER");
        admin.setDob(LocalDate.of(2004, 10, 8));
        admin.setIsDeleted(false);
        admin.setIsBlocked(false);
        admin.setRoles(List.of(adminRole));

        userRepository.save(admin);
    }
}