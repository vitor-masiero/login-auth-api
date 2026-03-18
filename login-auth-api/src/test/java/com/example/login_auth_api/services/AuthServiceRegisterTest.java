package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import com.example.login_auth_api.dto.response.RegisterResponseDTO;
import com.example.login_auth_api.exceptions.InvalidUserException;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceRegisterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve retornar sucesso quando registra usuário com campos válidos")
    void registrarUsuarioComCamposValidos() {
        var request = new RegisterRequestDTO(
                "João Silva",
                "joao@email.com",
                "11987654321",
                "Rua das Flores, 10",
                "senha123",
                UserRole.ROLE_USER
        );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.senha()))
                .thenReturn("senha-encriptada");

        //quando save for chamado com qualquer objeto, retorne esse objeto de volta
        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(tokenService.generateToken(any()))
                .thenReturn("jwt_token_mockado");

        RegisterResponseDTO response = authService.registerUser(request);

        assertThat(response).satisfies(r ->{
            assertThat(r.name()).isEqualTo("João Silva");
            assertThat(r.email()).isEqualTo("joao@email.com");
            assertThat(r.telefone()).isEqualTo("11987654321");
            assertThat(r.endereco()).isEqualTo("Rua das Flores, 10");
            assertThat(r.role()).isEqualTo(UserRole.ROLE_USER);
            assertThat(r.token()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando e-mail já estiver cadastrado")
    void registrarDeveLancarExcecaoQuandoEmailJaExiste() {
        var request = new RegisterRequestDTO(
                "João Silva",
                "joao@email.com",
                "11987654321",
                "Rua das Flores, 10",
                "senha123",
                UserRole.ROLE_USER
        );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.registerUser(request))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Usuário já existe. Faça login!");

        verify(userRepository, never()).save(any());
    }
}