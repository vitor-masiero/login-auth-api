package com.example.login_auth_api.dto.response;

import com.example.login_auth_api.domain.user.UserRole;

public record RegisterResponseDTO(
        String name,
        String email,
        String telefone,
        String endereco,
        UserRole role,
        String token
) {
}
