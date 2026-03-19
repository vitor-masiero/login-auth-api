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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String tokenUser;
    private String tokenAdmin;

    //Helpers para executar os testes
    @BeforeEach
    void setUp() {
        //Usuário com ROLE_USER
        User user = new User();
        user.setName("User Teste");
        user.setEmail("ct-security-user@test.com");
        user.setPassword(passwordEncoder.encode("senhasegura123"));
        user.setTelefone("11987654321");
        user.setEndereco("Rua dos Testes, 1");
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);
        tokenUser = tokenService.generateToken(user);

        User admin = new User();
        admin.setName("Admin Teste");
        admin.setEmail("ct-security-admin@test.com");
        admin.setPassword(passwordEncoder.encode("senhasegura123"));
        admin.setTelefone("11987654322");
        admin.setEndereco("Rua dos Testes, 2");
        admin.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(admin);
        tokenAdmin = tokenService.generateToken(admin);
    }

    //Removendo usuários criados para não poluir o banco
    @AfterEach
    void tearDown() {
        userRepository.findByEmail("ct-security-user@test.com").ifPresent(userRepository::delete);
        userRepository.findByEmail("ct-security-admin@test.com").ifPresent(userRepository::delete);
    }

    @Test
    @DisplayName("CT-21: Acesso a endpoint protegido sem token retorna 403")
    void ct21_acessoSemTokenRetorna403() throws Exception {
        mockMvc.perform(get("/user/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CT-22: Acesso a /admin/** com ROLE_USER retorna 403")
    void ct22_acessoAdminComRoleUserRetorna403() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CT-23: Acesso a /admin/** com ROLE_ADMIN retorna 200")
    void ct23_acessoAdminComRoleAdminRetorna200() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CT-31: DELETE /admin/users/{id} autenticado como ROLE_USER retorna 403")
    void ct31_exclusaoDeUsuarioComoUserRetorna403() throws Exception {
        mockMvc.perform(delete("/admin/users/qualquer-id")
                .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden());
    }

}
