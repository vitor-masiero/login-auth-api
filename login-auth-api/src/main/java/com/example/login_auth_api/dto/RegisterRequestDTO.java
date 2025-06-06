package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")String password,
        @NotBlank UserRole role
)

{
}
