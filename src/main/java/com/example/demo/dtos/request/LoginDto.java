package com.example.demo.dtos.request;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private char[] password;
}
