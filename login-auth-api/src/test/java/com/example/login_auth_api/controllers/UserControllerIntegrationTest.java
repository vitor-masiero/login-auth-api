package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import org.h2.command.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private String tokenUser;

    @BeforeEach
    void setUp() {
        //Usuário com ROLE_USER
        User user = new User();
        user.setName("User Teste");
        user.setEmail("ct-user@test.com");
        user.setPassword(passwordEncoder.encode("senhasegura123"));
        user.setTelefone("11987654321");
        user.setEndereco("Rua dos Testes, 1");
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);
        tokenUser = tokenService.generateToken(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.findByEmail("ct-user@test.com").ifPresent(userRepository::delete);
    }

    @Test
    @DisplayName("CT-24: GET /user/me retorna dados do usuário autenticado")
    void ct24_getPerfilRetornaDadosDoUsuarioAutenticado() throws Exception {

        mockMvc.perform(get("/user/me")
                 .header("Authorization", "Bearer " + tokenUser))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.email").value("ct-user@test.com"))
             .andExpect(jsonPath("$.id").exists())
             .andExpect(jsonPath("$.name").value("User Teste"))
             .andExpect(jsonPath("$.telefone").value("11987654321"))
             .andExpect(jsonPath("$.endereco").value("Rua dos Testes, 1"))
             .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("CT-25: GET /user/me não expõe o campo password na resposta")
    void ct25_getPerfilNaoExpoePassword() throws Exception {
        mockMvc.perform(get("/user/me")
                .header("Authorization", "Bearer " + tokenUser)).andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.senha").doesNotExist());
    }
}
