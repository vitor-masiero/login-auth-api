package com.example.login_auth_api.dto.response;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        String name,
        String email,
        String token
) {
}
