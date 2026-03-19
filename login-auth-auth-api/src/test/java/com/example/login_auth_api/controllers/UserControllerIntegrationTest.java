package com.example.login_auth_api.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================================================================
    // CT-24 | Integração | Prioridade: Alta
    // Busca do perfil retorna dados do usuário autenticado
    // =========================================================================
    // Dado que estou autenticado com token JWT válido do usuário "user@test.com"
    // Quando enviar GET /user/me
    // Então o sistema deve retornar status 200
    // E o corpo deve conter: id, nome, email, telefone, endereço, role
    @Test
    @DisplayName("CT-24: GET /user/me retorna dados do usuário autenticado")
    void ct24_getPerfilRetornaDadosDoUsuarioAutenticado() throws Exception {
        // TODO: implementar
        // String token = registrarELogar("user@test.com", "Senha@123");
        // mockMvc.perform(get("/user/me")
        //         .header("Authorization", "Bearer " + token))
        //     .andExpect(status().isOk())
        //     .andExpect(jsonPath("$.email").value("user@test.com"))
        //     .andExpect(jsonPath("$.id").exists())
        //     .andExpect(jsonPath("$.name").exists())
        //     .andExpect(jsonPath("$.telefone").exists())
        //     .andExpect(jsonPath("$.endereco").exists())
        //     .andExpect(jsonPath("$.role").exists());
    }

    // =========================================================================
    // CT-25 | Integração | Prioridade: Alta
    // Resposta de perfil não expõe o campo password
    // =========================================================================
    // Dado que estou autenticado com token JWT válido
    // Quando enviar GET /user/me
    // Então o sistema deve retornar status 200
    // E o corpo da resposta NÃO deve conter o campo "password" nem "senha"
    @Test
    @DisplayName("CT-25: GET /user/me não expõe o campo password na resposta")
    void ct25_getPerfilNaoExpoePassword() throws Exception {
        // TODO: implementar
        // String token = registrarELogar("user@test.com", "Senha@123");
        // mockMvc.perform(get("/user/me")
        //         .header("Authorization", "Bearer " + token))
        //     .andExpect(status().isOk())
        //     .andExpect(jsonPath("$.password").doesNotExist())
        //     .andExpect(jsonPath("$.senha").doesNotExist());
    }
}
