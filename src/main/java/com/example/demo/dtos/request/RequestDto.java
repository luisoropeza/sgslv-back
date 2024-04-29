package com.example.demo.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RequestDto {
    @NotBlank(message = "Reason is mandatory")
    @Pattern(regexp = "^(Medical license|Vacation|Other)$", message = "Bad reason")
    private String reason;
    @NotNull(message = "Init date is mandatory")
    @Future(message = "Start date must be after the current date")
    private LocalDate initDate;
    @NotNull(message = "End date is mandatory")
    @Future(message = "End date must be after the current date")
    private LocalDate endDate;
    @Pattern(regexp = "^(|.{1,255})$", message = "Description can't be more than 255 characters")
    private String description;
}
