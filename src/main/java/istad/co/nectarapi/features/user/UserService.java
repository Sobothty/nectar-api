package istad.co.nectarapi.features.user;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.features.user.dto.UserResponse;
import istad.co.nectarapi.features.user.dto.UserUpdate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    BasedMessage assignAdminRole(String email);
    BasedMessage removeAdminRole(String email);

    UserResponse getProfile(Jwt jwt);
    UserResponse editProfile(Jwt jwt, UserUpdate userUpdate);
}
