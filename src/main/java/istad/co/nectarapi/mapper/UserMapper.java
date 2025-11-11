package istad.co.nectarapi.mapper;

import istad.co.nectarapi.audit.AuditMapper;
import istad.co.nectarapi.audit.AuditResponse;
import istad.co.nectarapi.audit.Auditable;
import istad.co.nectarapi.domain.Role;
import istad.co.nectarapi.domain.User;
import istad.co.nectarapi.features.user.dto.UserResponse;
import istad.co.nectarapi.features.user.dto.UserUpdate;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AuditMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "audit", source = "user")
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserUpdate userUpdate, @MappingTarget User user);

    // Custom method to convert List<Role> -> List<String>
    default List<String> mapRoles(List<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name()) // RoleName is an enum
                .collect(Collectors.toList());
    }


}
