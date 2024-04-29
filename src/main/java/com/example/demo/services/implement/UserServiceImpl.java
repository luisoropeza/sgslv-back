package com.example.demo.services.implement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.request.CreateUserDto;
import com.example.demo.dtos.request.UpdateProfileDto;
import com.example.demo.dtos.request.UpdateUserDto;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.dtos.response.UserUpdateResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.models.Team;
import com.example.demo.models.User;
import com.example.demo.models.UserDetail;
import com.example.demo.repositories.jpa.TeamRepository;
import com.example.demo.repositories.jpa.UserDetailRepository;
import com.example.demo.repositories.jpa.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.jwt.JwtUtils;
import com.example.demo.services.mapper.MainMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final TeamRepository teamRepository;
    private final MainMapper mainMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserDto dto) {
        if (dto.getUsername() != null && userRepository.existsByUsername(dto.getUsername())) {
            throw new AppException("That's username is already exist", HttpStatus.BAD_REQUEST);
        }
        if (dto.getEmail() != null && userDetailRepository.existsByEmail(dto.getEmail())) {
            throw new AppException("That's email is already exist", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPhone() != null && userDetailRepository.existsByPhone(dto.getPhone())) {
            throw new AppException("That's phone number is already exist", HttpStatus.BAD_REQUEST);
        }
        Team team = null;
        if (dto.getTeam() != null) {
            team = teamRepository.findById(dto.getTeam())
                    .orElseThrow(() -> new AppException("Team can't be found", HttpStatus.NOT_FOUND));
        }
        User user = mainMapper.toUser(dto, team, passwordEncoder);
        userRepository.save(user);
        return mainMapper.toUserResponse(user);
    }

    @Override
    public UserUpdateResponse updateUser(UpdateProfileDto dto, String token) {
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        UserDetail userDetail = userDetailRepository.findByUser(user).orElse(new UserDetail());
        existUsernameEmailPhoneUpdate(dto, user, userDetail);
        user = mainMapper.toUser(user, userDetail, dto, passwordEncoder);
        userRepository.save(user);
        String newToken = jwtUtils.generateAccesToken(dto.getUsername());
        return mainMapper.toUserUpdateResponse(user, newToken);
    }

    @Override
    public UserResponse updateUserById(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        Team team = null;
        if (dto.getTeam() != null) {
            team = teamRepository.findById(dto.getTeam())
                    .orElseThrow(() -> new AppException("Team can't be found", HttpStatus.NOT_FOUND));
        }
        user = mainMapper.toUser(user, dto, team, passwordEncoder);
        userRepository.save(user);
        return mainMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUser(String token) {
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return mainMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return mainMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(mainMapper::toUserResponse).toList();
    }

    @Override
    public List<UserResponse> getUsersByTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new AppException("Team can't be found", HttpStatus.NOT_FOUND));
        return userRepository.findAll().stream().filter(user -> team.equals(user.getTeam()))
                .map(mainMapper::toUserResponse).toList();
    }

    public void existUsernameEmailPhoneUpdate(UpdateProfileDto dto, User user, UserDetail userDetail) {
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new AppException("That's username is already exist", HttpStatus.BAD_REQUEST);
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(userDetail.getEmail())
                && userDetailRepository.existsByEmail(dto.getEmail())) {
            throw new AppException("That's email is already exist", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPhone() != null && !dto.getPhone().equals(userDetail.getPhone())
                && userDetailRepository.existsByPhone(dto.getPhone())) {
            throw new AppException("That's phone number is already exist", HttpStatus.BAD_REQUEST);
        }
    }
}
