package istad.co.nectarapi.features.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{email}/assign-admin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')") // Only admins can assign admin role
    public void assignAdminRole(@PathVariable String email) {
        userService.assignAdminRole(email);
    }

    @PutMapping("/{email}/remove-admin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')") // Only admins can remove admin role
    public void removeAdminRole(@PathVariable String email) {
        userService.removeAdminRole(email);
    }
}