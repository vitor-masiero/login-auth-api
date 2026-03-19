package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import com.example.login_auth_api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthRegisterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("CT-03: Cadastro com campo obrigatório ausente retorna 400")
    void ct03_cadastroSemCampoObrigatorioRetorna400() throws Exception {
        RegisterRequestDTO body = new RegisterRequestDTO(
                null,
                "jose@gmail.com",
                "48999064470",
                "Rua da Misericórdia",
                "senhasegura123",
                UserRole.ROLE_USER
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O nome é obrigatório"));
    }

    @Test
    @DisplayName("CT-04: Cadastro com e-mail inválido retorna 400")
    void ct04_cadastroComEmailInvalidoRetorna400() throws Exception {
        RegisterRequestDTO body = new RegisterRequestDTO(
                "José Vitor",
                "jose@.com", //email inválido
                "48999064470",
                "Rua da Misericórdia",
                "senhasegura123",
                UserRole.ROLE_USER
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("O e-mail deve ser válido"));
    }

    @Test
    @DisplayName("CT-05: Cadastro com telefone fora do padrão retorna 400")
    void ct05_cadastroComTelefoneInvalidoRetorna400() throws Exception {
        RegisterRequestDTO body = new RegisterRequestDTO(
                "José Vitor",
                "jose@gmail.com",
                "(48)999064470",
                "Rua da Misericórdia",
                "senhasegura123",
                UserRole.ROLE_USER
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.telefone").value("O telefone deve conter apenas números (10 ou 11 dígitos)"));
    }

    @Test
    @DisplayName("CT-06: Cadastro com senha abaixo de 8 caracteres retorna 400")
    void ct06_cadastroComSenhaFracaRetorna400() throws Exception {
        RegisterRequestDTO body = new RegisterRequestDTO(
                "José Vitor",
                "jose@gmail.com",
                "48999064470",
                "Rua da Misericórdia",
                "senha", //abaixo de 8 caracteres
                UserRole.ROLE_USER
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senha").value("A senha deve ter pelo menos 8 caracteres"));
    }

    @Test
    @DisplayName("CT-07: Cadastro com role ADMIN no corpo deve ser rejeitado com 400")
    void ct07_cadastroComRoleAdminDeveSerRejeitado() throws Exception {
        String emailTeste = "ct07-admin-escalation@test.com";

        RegisterRequestDTO body = new RegisterRequestDTO(
                "José Vitor",
                emailTeste,
                "48999064470",
                "Rua da Misericórdia",
                "senhadeteste123",
                UserRole.ROLE_ADMIN
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

        userRepository.findByEmail(emailTeste)
                .ifPresent(user ->
                        assertThat(user.getRole()).isNotEqualTo(UserRole.ROLE_ADMIN)
                );
    }
}
