package com.example.login_auth_api.dto.response;

import com.example.login_auth_api.domain.user.UserRole;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String telefone,
        String endereco,
        UserRole role
) {
}
