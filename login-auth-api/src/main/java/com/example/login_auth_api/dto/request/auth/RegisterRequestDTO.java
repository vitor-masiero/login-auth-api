package com.example.login_auth_api.dto.request.auth;

import com.example.login_auth_api.domain.user.UserRole;
import jakarta.validation.constraints.*;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ser válido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
        String telefone,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        @NotNull(message = "A role é obrigatória (ROLE_USER ou ROLE_ADMIN)")
        UserRole role
) {}

