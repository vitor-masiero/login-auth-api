package com.example.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FornecedorRequestDTO(
        @NotNull Integer idFornecedor,
        @NotBlank String dsRazaoSocial,
        @NotBlank String dtHorarioFunc
        ) {
}
