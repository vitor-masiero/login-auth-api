package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.user.UserRole;

public record ResponseDTO(
        String nmUsuario,
        String token, UserRole enRole) {
}
