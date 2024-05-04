package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.CreateUserDto;
import com.example.demo.dtos.request.UpdateProfileDto;
import com.example.demo.dtos.request.UpdateUserDto;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.dtos.response.UserUpdateResponse;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserDto dto) {
        UserResponse user = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).body(user);
    }

    @PostMapping("/users/import")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> importUsers(
            @RequestBody Map<String, List<CreateUserDto>> list) {
        return ResponseEntity.ok(userService.importUsers(list.get("users")));
    }

    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','PERSONAL','EMPLOYEE')")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestBody @Valid UpdateProfileDto dto,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.updateUser(dto, token));
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserDto dto) {
        return ResponseEntity.ok(userService.updateUserById(id, dto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','PERSONAL','EMPLOYEE')")
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.getUser(token));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PERSONAL')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("team/{id}/users")
    @PreAuthorize("hasAnyRole('ADMIN','PERSONAL')")
    public ResponseEntity<List<UserResponse>> getUsersByTeam(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUsersByTeam(id));
    }
}
