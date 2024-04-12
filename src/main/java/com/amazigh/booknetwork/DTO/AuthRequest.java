package com.amazigh.booknetwork.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthRequest(
    @Email(message = "Email is not formated")
    @NotEmpty(message = "Email can not be empty")
    @NotBlank(message = "Email can not be empty")
    String email,
    @Size(min = 8, message = "Password should be 8 characters minimum")
    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password can not be empty")
    String password
) { }
