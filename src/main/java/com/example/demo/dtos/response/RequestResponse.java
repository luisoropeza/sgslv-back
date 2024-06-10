package com.example.demo.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RequestResponse {
    private Long id;
    private String status;
    private String reason;
    private LocalDate initDate;
    private LocalDate endDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private DocumentResponse document;
    private UserResponse approvedBy;
    private UserResponse user;
}
