package com.amazigh.booknetwork.DTO;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotEmpty(message = "First name can not be empty")
    @NotBlank(message = "First name can not be empty")
    String firstname,
    @NotEmpty(message = "last name can not be empty")
    @NotBlank(message = "last name can not be empty")
    String lastname,
    @Email(message = "Email is not formated")
    @NotEmpty(message = "Email can not be empty")
    @NotBlank(message = "Email can not be empty")
    String email,
    @Size(min = 8, message = "Password should be 8 characters minimum")
    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password can not be empty")
    String password
) { }
