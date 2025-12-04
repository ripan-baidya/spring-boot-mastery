package in.ripanbaidya.openapidoc.controller;

import in.ripanbaidya.openapidoc.config.OpenApiConfig;
import in.ripanbaidya.openapidoc.dto.CreateUserRequest;
import in.ripanbaidya.openapidoc.dto.UpdateUserRequest;
import in.ripanbaidya.openapidoc.dto.UserDto;
import in.ripanbaidya.openapidoc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
// Tag Configuration Behavior: Tags defined in OpenApiConfig serve as the base configuration
// Using @Tag annotation here will either:
// 1. Create a new tag group if the name doesn't exist in OpenApiConfig
// 2. Override the description if the tag name matches an existing one
// Example: Uncomment to customize this controller's tag
// @Tag(name = "Demo User API", description = "This is my custom tag for UserController")
@Tag(name = "Users", description = "This is the User API")
public class UserController {

    private final UserService userService;

    @Operation(
            method = "GET", // We dont need to add method info, swagger already detect the methods from @GetMapping, @PostMapping, etc.
            summary = "Get all users",
            description = "Retrieve a paginated list of all users",
            tags = {"Users"} // If we use tags info at the class level, do not need to repeat it here
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users",
                    // Here "content" describes media type + the shape(schema) of the response body
                    content = @Content(array = @ArraySchema(schema = @Schema(
                            implementation = UserDto.class,
                            // Note: If you declare produces = MediaType.APPLICATION_JSON_VALUE at the class level,
                            // you DO NOT need to repeat contentMediaType in your OpenAPI annotations.
                            contentMediaType = MediaType.APPLICATION_JSON_VALUE
                    )))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden"
            )
    })
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @Parameter(description = "Page number (0-based)", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort by field")
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable Long id) {

        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided details"
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class))
            )
            @Valid @RequestBody CreateUserRequest request) {

        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
            summary = "Update user",
            description = "Update an existing user's information"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", example = "123") @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user by ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
