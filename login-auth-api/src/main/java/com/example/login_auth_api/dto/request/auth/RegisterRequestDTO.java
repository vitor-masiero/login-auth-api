package com.example.login_auth_api.dto.request.auth;

import com.example.login_auth_api.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank
        @Size (min = 2, max = 255, message = "O nome deve ter no mínimo 2 caracteres.")
        String nome,

        @NotBlank
        @Email (message = "O e-mail deve ser válido")
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        String endereco,

        @NotBlank @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        @NotBlank
        UserRole role
)

{
}
