package in.ripanbaidya.jwtauth.controller;

import in.ripanbaidya.jwtauth.dto.ChangePasswordRequest;
import in.ripanbaidya.jwtauth.dto.UpdateProfileRequest;
import in.ripanbaidya.jwtauth.entity.User;
import in.ripanbaidya.jwtauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile() {
        User user = userService.getCurrentUser();
        Map<String, Object> body = Map.of(
                "id", user.getId(),
                "first_name", user.getFirstName(),
                "last_name", user.getLastName(),
                "email", user.getEmail(),
                "password", user.getPassword()
        );
        return  ResponseEntity.ok(body);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.getCurrentUser();
        User updateUserProfile = userService.updateUserProfile(user.getId(), request);

        Map<String, Object> body = Map.of(
                "id", updateUserProfile.getId(),
                "first_name", updateUserProfile.getFirstName(),
                "last_name", updateUserProfile.getLastName(),
                "email", updateUserProfile.getEmail(),
                "password", updateUserProfile.getPassword()
        );
        return  ResponseEntity.ok(body);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteProfile() {
        User user = userService.getCurrentUser();
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User user = userService.getCurrentUser();
        userService.changePassword(user.getId(), request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
