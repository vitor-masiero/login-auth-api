package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private String tokenAdmin;
    private String userId;
    private String userEmail;

    @BeforeEach
    void setUp() {
        User admin = new User();
        admin.setName("Admin Teste");
        admin.setEmail("ct-admin@test.com");
        admin.setPassword(passwordEncoder.encode("senhasegura123"));
        admin.setTelefone("11987654322");
        admin.setEndereco("Rua dos Testes, 2");
        admin.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(admin);
        tokenAdmin = tokenService.generateToken(admin);

        User user = new User();
        userEmail = "user@test.com";

        user.setName("User Teste");
        user.setEmail(userEmail);
        user.setPassword(passwordEncoder.encode("senhasegura123"));
        user.setTelefone("11987654322");
        user.setEndereco("Rua dos Testes, 2");
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);

        userId = user.getId();

    }

    @AfterEach
    void tearDown() {
        userRepository.findByEmail("ct-admin@test.com").ifPresent(userRepository::delete);
    }

    @Test
    @DisplayName("CT-27: GET /admin/users autenticado como ADMIN retorna 200 com lista de usuários")
    void ct27_listagemDeUsuariosComoAdminRetorna200() throws Exception {
        mockMvc.perform(get("/admin/users?page=0&size=10")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].telefone").exists())
                .andExpect(jsonPath("$[0].endereco").exists())
                .andExpect(jsonPath("$[0].role").exists());

    }

    @Test
    @DisplayName("GET /admin/delete/{id} autenticado como ADMIN retorna 204 no-content e deleta do banco")
    void deleteAutenticadoComoAdminRetorna204() throws Exception{

        mockMvc.perform(delete("/admin/users/" + userId)
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findByEmail(userEmail)).isEmpty();
    }
}
