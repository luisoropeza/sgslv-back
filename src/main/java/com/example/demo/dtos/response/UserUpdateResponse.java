package com.example.demo.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserUpdateResponse {
    
    private Long id;
    private String username;
    private String firstName;
    private String role;
    private TeamResponse team;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDay;
    private LocalDateTime createdAt;
    private String token;
}
