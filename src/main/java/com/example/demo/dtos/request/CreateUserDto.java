package com.example.demo.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^\\S*$", message = "Username can't contain spaces")
    @Pattern(regexp = "^(|.{5,})$", message = "Username can't be less 5 characters")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^\\S*$", message = "Password can't contain spaces")
    @Pattern(regexp = "^(|.{5,})$", message = "Password can't be less 5 characters")
    private String password;
    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "^(ADMIN|EMPLOYEE|PERSONAL)$", message = "Role should be one of ADMIN, EMPLOYEE, or PERSONAL")
    private String role;
    private String team;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^\\+[1-9]\\d{1,3} \\d{4,14}$", message = "Invalid phone number")
    private String phone;
    private LocalDate birthDay;
}
