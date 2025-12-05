package in.ripanbaidya.jwtauth.service;

import in.ripanbaidya.jwtauth.dto.ChangePasswordRequest;
import in.ripanbaidya.jwtauth.dto.SignupRequest;
import in.ripanbaidya.jwtauth.dto.UpdateProfileRequest;
import in.ripanbaidya.jwtauth.entity.User;
import in.ripanbaidya.jwtauth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        return userRepository.save(user);
    }

    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated!");
        }

        String email = authentication.getName(); // Here, Email is works as username
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
    }

    /**
     * Get current user by his id
     */
    public User getUserById(Long userId) {
        User currentUser = getCurrentUser();

        // Check if user access his own profile
        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("Access denied!");
        }
        return userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + currentUser.getEmail()));
    }

    @Transactional
    public User updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);

        if (request.firstName() != null ) user.setFirstName(request.firstName());
        if (request.lastName() != null ) user.setLastName(request.lastName());

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getUserById(userId);

        // verify current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {}

        // update the current password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}