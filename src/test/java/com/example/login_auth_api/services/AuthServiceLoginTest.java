package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.dto.request.auth.LoginRequestDTO;
import com.example.login_auth_api.dto.response.LoginResponseDTO;
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
import static org.mockito.Mockito.*;

public class AuthServiceLoginTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private AuthService authService;

    private User usuarioExistente;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        usuarioExistente = new User();
        usuarioExistente.setName("João Silva");
        usuarioExistente.setEmail("joao@email.com");
        usuarioExistente.setPassword("senha-criptografada");
        usuarioExistente.setRole(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("CT-08: Deve retornar token quando credenciais forem válidas")
    void ct08_deveRetornarTokenQuandoCredenciaisValidas() {
        var request = new LoginRequestDTO("joao@email.com", "senha123");

        when(userRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.matches("senha123", "senha-criptografada"))
                .thenReturn(true);                          // senha bate
        when(tokenService.generateToken(usuarioExistente))
                .thenReturn("jwt-token-fake");

        LoginResponseDTO response = authService.login(request);

        assertThat(response.email()).isEqualTo("joao@email.com");
        assertThat(response.name()).isEqualTo("João Silva");
        assertThat(response.token()).isEqualTo("jwt-token-fake");
    }

    @Test
    @DisplayName("CT-10: Deve lançar InvalidUserException quando e-mail não existir")
    void ct10_deveLancarExcecaoQuandoEmailNaoEncontrado() {
        var request = new LoginRequestDTO("naoexiste@email.com", "senha123");

        when(userRepository.findByEmail("naoexiste@email.com"))
                .thenReturn(Optional.empty());              // e-mail não está no banco

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Usuário ou senha inválidos");

        // Se o e-mail não existe, nem deve tentar comparar a senha
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("CT-09: Deve lançar InvalidUserException quando senha for incorreta")
    void ct09_deveLancarExcecaoQuandoSenhaIncorreta() {
        var request = new LoginRequestDTO("joao@email.com", "senha-errada");

        when(userRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioExistente)); // e-mail existe
        when(passwordEncoder.matches("senha-errada", "senha-criptografada"))
                .thenReturn(false);                         // mas senha não bate

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Usuário ou senha inválidos");

        // Token nunca deve ser gerado se a senha estiver errada
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    @DisplayName("CT-12: Mensagem de erro não revela se o email existe")
    void ct12_deveRetornarMensagemDeErroGenerica() {
        var request = new LoginRequestDTO("naoexiste@email.com", "senha123");

        when(userRepository.findByEmail("naoexiste@email.com"))
                .thenReturn(Optional.empty());              // e-mail não está no banco

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Usuário ou senha inválidos");

        // Se o e-mail não existe, nem deve tentar comparar a senha
        verify(passwordEncoder, never()).matches(any(), any());
    }

}
