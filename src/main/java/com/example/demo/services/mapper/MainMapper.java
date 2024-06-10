package com.example.demo.services.mapper;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dtos.request.RequestDto;
import com.example.demo.dtos.request.TeamDto;
import com.example.demo.dtos.request.UpdateProfileDto;
import com.example.demo.dtos.request.UpdateUserDto;
import com.example.demo.dtos.request.CreateUserDto;
import com.example.demo.dtos.response.AuthResponse;
import com.example.demo.dtos.response.DocumentResponse;
import com.example.demo.dtos.response.RequestResponse;
import com.example.demo.dtos.response.TeamResponse;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.dtos.response.UserUpdateResponse;
import com.example.demo.models.Document;
import com.example.demo.models.Request;
import com.example.demo.models.Role;
import com.example.demo.models.Status;
import com.example.demo.models.Team;
import com.example.demo.models.User;
import com.example.demo.models.UserDetail;

@Component
public class MainMapper {
    public Team toTeam(TeamDto dto) {
        Team team = new Team();
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        return team;
    }

    public Team toTeam(Team team, TeamDto dto) {
        team.setName(dto.getName());
        team.setDescription(dto.getDescription());
        return team;
    }

    public TeamResponse toTeamResponse(Team team) {
        if (team != null) {
            TeamResponse dto = new TeamResponse();
            dto.setId(team.getId());
            dto.setName(team.getName());
            dto.setDescription((team.getDescription()));
            return dto;
        }
        return null;
    }

    public User toUser(CreateUserDto dto, Team team, PasswordEncoder passwordEncoder) {
        User user = new User();
        UserDetail userDetail = new UserDetail();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
        user.setRole(Role.valueOf(dto.getRole()));
        user.setTeam(team);
        user.setCreatedAt(LocalDateTime.now());
        userDetail.setFirstName(dto.getFirstName().trim());
        userDetail.setLastName(dto.getLastName().trim());
        userDetail.setEmail(dto.getEmail());
        userDetail.setPhone(dto.getPhone());
        userDetail.setBirthDay(dto.getBirthDay());
        userDetail.setUser(user);
        user.setUserDetail(userDetail);
        return user;
    }

    public User toUser(User user, UserDetail userDetail, UpdateProfileDto dto, PasswordEncoder passwordEncoder) {
        user.setUsername(dto.getUsername().trim());
        if (dto.getPassword() != null) {
            if (!dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
            }
        }
        userDetail.setFirstName(dto.getFirstName().trim());
        userDetail.setLastName(dto.getLastName().trim());
        userDetail.setEmail(dto.getEmail());
        userDetail.setPhone(dto.getPhone());
        userDetail.setBirthDay(dto.getBirthDay());
        userDetail.setUser(user);
        user.setUserDetail(userDetail);
        return user;
    }

    public User toUser(User user, UpdateUserDto dto, Team team, PasswordEncoder passwordEncoder) {
        if (dto.getPassword() != null) {
            if (!dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
            }
        }
        user.setRole(Role.valueOf(dto.getRole()));
        user.setTeam(team);
        return user;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setTeam(toTeamResponse(user.getTeam()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setFirstName(user.getUserDetail().getFirstName());
        dto.setLastName(user.getUserDetail().getLastName());
        dto.setEmail(user.getUserDetail().getEmail());
        dto.setPhone(user.getUserDetail().getPhone());
        dto.setBirthDay(user.getUserDetail().getBirthDay());
        return dto;
    }

    public UserUpdateResponse toUserUpdateResponse(User user, String token) {
        UserUpdateResponse dto = new UserUpdateResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setTeam(toTeamResponse(user.getTeam()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setFirstName(user.getUserDetail().getFirstName());
        dto.setLastName(user.getUserDetail().getLastName());
        dto.setEmail(user.getUserDetail().getEmail());
        dto.setPhone(user.getUserDetail().getPhone());
        dto.setBirthDay(user.getUserDetail().getBirthDay());
        dto.setToken(token);
        return dto;
    }

    public Request toRequest(RequestDto dto, User user, Map<String, Object> file) {
        Request request = new Request();
        Document document = new Document();
        request.setStatus(Status.Pending);
        request.setReason(dto.getReason());
        request.setInitDate(dto.getInitDate());
        request.setEndDate(dto.getEndDate());
        request.setDescription(dto.getDescription());
        request.setCreatedAt(LocalDateTime.now());
        request.setUser(user);
        if (file != null) {
            document.setName(file.get("public_id").toString());
            document.setUrl(file.get("url").toString());
            document.setRequest(request);
            request.setDocument(document);
        }
        return request;
    }

    public Request toRequest(Request request, RequestDto dto, Map<String, Object> file) {
        Document document = new Document();
        request.setReason(dto.getReason());
        request.setInitDate(dto.getInitDate());
        request.setEndDate(dto.getEndDate());
        request.setDescription(dto.getDescription());
        if (file != null) {
            document.setName(file.get("public_id").toString());
            document.setUrl(file.get("url").toString());
            document.setRequest(request);
            request.setDocument(document);
        }
        return request;
    }

    public RequestResponse toRequestResponse(Request request) {
        RequestResponse dto = new RequestResponse();
        dto.setId(request.getId());
        dto.setStatus(request.getStatus().name());
        dto.setReason(request.getReason());
        dto.setInitDate(request.getInitDate());
        dto.setEndDate(request.getEndDate());
        dto.setDescription(request.getDescription());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setApprovedAt(request.getApprovedAt());
        dto.setDocument(toDocumentResponse(request.getDocument()));
        if(request.getApprovedBy() != null) {
            dto.setApprovedBy(toUserResponse(request.getApprovedBy()));
        }
        dto.setUser(toUserResponse(request.getUser()));
        return dto;
    }

    private DocumentResponse toDocumentResponse(Document document) {
        if (document != null) {
            DocumentResponse dto = new DocumentResponse();
            dto.setName(document.getName());
            dto.setUrl(document.getUrl());
            return dto;
        }
        return null;
    }

    public AuthResponse toAuthResponse(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        return authResponse;
    }
}
