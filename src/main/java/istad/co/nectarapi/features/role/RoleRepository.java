package istad.co.nectarapi.features.role;

import istad.co.nectarapi.domain.Role;
import istad.co.nectarapi.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
