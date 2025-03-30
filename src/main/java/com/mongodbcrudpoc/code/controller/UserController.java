package com.mongodbcrudpoc.code.controller;

import com.mongodbcrudpoc.code.model.User;
import com.mongodbcrudpoc.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Operations pertaining to user management")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User[].class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            }
    )
    public List<User> getUsers() {
        List<User> users = userService.getAllUsers();
        return users;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a user by ID",
            description = "Returns a single user by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    public ResponseEntity<User> getUserById(
            @Parameter(in = ParameterIn.PATH, description = "ID of the user to retrieve", required = true)
            @PathVariable String id) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(id));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the created object",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public ResponseEntity<User> createUser(
            @Parameter(description = "User object to be created", required = true, schema = @Schema(implementation = User.class))
            @RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    public ResponseEntity<String> deleteUser(
            @Parameter(in = ParameterIn.PATH, description = "ID of the user to delete", required = true)
            @PathVariable String id) {
        boolean deleted = userService.deleteUserById(id);
        return deleted ? ResponseEntity.ok("User deleted successfully.") : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a user",
            description = "Updates an existing user with provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    public ResponseEntity<String> updateUser(
            @Parameter(in = ParameterIn.PATH, description = "ID of the user to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated user object", required = true, schema = @Schema(implementation = User.class))
            @RequestBody User updatedUser) {
        boolean updated = userService.updateUserById(id, updatedUser);
        return updated ? ResponseEntity.ok("User updated successfully.") : ResponseEntity.notFound().build();
    }
}
