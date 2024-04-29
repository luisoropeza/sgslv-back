package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.request.CreateUserDto;
import com.example.demo.dtos.request.UpdateProfileDto;
import com.example.demo.dtos.request.UpdateUserDto;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.dtos.response.UserUpdateResponse;

public interface UserService {
    UserResponse createUser(CreateUserDto dto);

    UserUpdateResponse updateUser(UpdateProfileDto dto, String token);

    UserResponse updateUserById(Long id, UpdateUserDto dto);

    UserResponse getUser(String username);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersByTeam(Long id);
}
