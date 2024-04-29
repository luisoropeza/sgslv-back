package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.LoginDto;
import com.example.demo.dtos.response.AuthResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.models.User;
import com.example.demo.repositories.jpa.UserRepository;
import com.example.demo.services.AuthService;
import com.example.demo.services.jwt.JwtUtils;
import com.example.demo.services.mapper.MainMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import java.nio.CharBuffer;

@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MainMapper mainMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException("Uknown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(request.getPassword()), user.getPassword())) {
            String token = jwtUtils.generateAccesToken(user.getUsername());
            return mainMapper.toAuthResponse(token);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }
}
