package com.example.login_auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank String nmProduto,
        @NotNull BigDecimal vlProduto,
        String dsProduto,
        @NotBlank String dsEmail
        ) {}
