package com.example.demo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
}
