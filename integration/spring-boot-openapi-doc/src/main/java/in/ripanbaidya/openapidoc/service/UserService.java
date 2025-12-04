package in.ripanbaidya.openapidoc.service;

import in.ripanbaidya.openapidoc.dto.CreateUserRequest;
import in.ripanbaidya.openapidoc.dto.UpdateUserRequest;
import in.ripanbaidya.openapidoc.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Dummy Service
@Service
public class UserService {


    public Page<UserDto> getAllUsers(Pageable pageable) {
        return null;
    }

    public UserDto getUserById(Long id) {
        return null;
    }

    public UserDto createUser(CreateUserRequest request) {
        return null;
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        return null;
    }

    public void deleteUser(Long id) {
    }
}
