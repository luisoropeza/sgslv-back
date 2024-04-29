package com.example.demo.services;

import com.example.demo.dtos.request.LoginDto;
import com.example.demo.dtos.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginDto request);
}
