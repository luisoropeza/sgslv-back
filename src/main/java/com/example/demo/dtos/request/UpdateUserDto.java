package com.example.demo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserDto {
    @Pattern(regexp = "^\\S*$", message = "Password can't contain spaces")
    @Pattern(regexp = "^(|.{5,})$", message = "Password can't be less 5 characters")
    private String password;
    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "^(ADMIN|EMPLOYEE|PERSONAL)$", message = "Role should be one of ADMIN, EMPLOYEE, or PERSONAL")
    private String role;
    private Long team;
}
