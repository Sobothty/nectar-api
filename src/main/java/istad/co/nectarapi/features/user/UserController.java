package istad.co.nectarapi.features.user;

import istad.co.nectarapi.features.user.dto.UserResponse;
import istad.co.nectarapi.features.user.dto.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{email}/assign-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignAdminRole(@PathVariable String email) {
        return new ResponseEntity<>(userService.assignAdminRole(email), HttpStatus.OK);
    }

    @PutMapping("/{email}/remove-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAdminRole(@PathVariable String email) {
        return new ResponseEntity<>(userService.removeAdminRole(email), HttpStatus.OK);
    }

    @GetMapping("/me")
    public UserResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        return userService.getProfile(jwt);
    }

    @PutMapping("/me")
    public UserResponse editProfile(@AuthenticationPrincipal Jwt jwt,
                                    @RequestBody UserUpdate userUpdate) {
        return userService.editProfile(jwt, userUpdate);
    }
}