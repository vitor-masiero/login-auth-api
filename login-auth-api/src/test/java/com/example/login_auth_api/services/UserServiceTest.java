package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.dto.request.user.UserUpdateDTO;
import com.example.login_auth_api.dto.response.UserResponseDTO;
import com.example.login_auth_api.exceptions.ResourceNotFoundException;
import com.example.login_auth_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User usuarioExistente;

    @BeforeEach
    void setUp() {
        usuarioExistente = new User();
        usuarioExistente.setId("user-id-1");
        usuarioExistente.setName("João Silva");
        usuarioExistente.setEmail("joao@email.com");
        usuarioExistente.setTelefone("11987654321");
        usuarioExistente.setEndereco("Rua das Flores, 10");
        usuarioExistente.setRole(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("buscarUserPorId: deve retornar os dados do usuário quando ID existir")
    void buscarUserPorId_deveRetornarDadosQuandoIdExistir() {
        when(userRepository.findById("user-id-1"))
                .thenReturn(Optional.of(usuarioExistente));

        UserResponseDTO response = userService.buscarUserPorId("user-id-1");

        assertThat(response.id()).isEqualTo("user-id-1");
        assertThat(response.name()).isEqualTo("João Silva");
        assertThat(response.email()).isEqualTo("joao@email.com");
        assertThat(response.telefone()).isEqualTo("11987654321");
        assertThat(response.endereco()).isEqualTo("Rua das Flores, 10");
        assertThat(response.role()).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("buscarUserPorId: deve lançar ResourceNotFoundException quando ID não existir")
    void buscarUserPorId_deveLancarExcecaoQuandoIdNaoExistir() {
        when(userRepository.findById("id-inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.buscarUserPorId("id-inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com o ID: id-inexistente");
    }

    @Test
    @DisplayName("CT-29: updateUser deve persistir os dados atualizados e retornar o DTO correto")
    void ct29_updateUser_devePersistirDadosAtualizadosERetornarDTO() {
        var dto = new UserUpdateDTO("Novo Nome", "11999999999", "Nova Rua, 100");

        when(userRepository.findById("user-id-1"))
                .thenReturn(Optional.of(usuarioExistente));
        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserResponseDTO response = userService.updateUser(dto, "user-id-1");

        verify(userRepository, times(1)).save(any());

        assertThat(response.name()).isEqualTo("Novo Nome");
        assertThat(response.telefone()).isEqualTo("11999999999");
        assertThat(response.endereco()).isEqualTo("Nova Rua, 100");
        assertThat(response.email()).isEqualTo("joao@email.com");
        assertThat(response.role()).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("updateUser: deve lançar ResourceNotFoundException quando ID não existir")
    void updateUser_deveLancarExcecaoQuandoIdNaoExistir() {
        var dto = new UserUpdateDTO("Novo Nome", "11999999999", "Nova Rua, 100");

        when(userRepository.findById("id-inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(dto, "id-inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com o ID: id-inexistente");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT-34: delete deve chamar o repositório uma vez com o objeto correto")
    void ct34_delete_deveChamarRepositorioUmaVezComUsuarioCorreto() {
        when(userRepository.findById("user-id-1"))
                .thenReturn(Optional.of(usuarioExistente));
        doNothing().when(userRepository).delete(any());

        assertThatCode(() -> userService.delete("user-id-1"))
                .doesNotThrowAnyException();

        var captor = org.mockito.ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).delete(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("user-id-1");
    }

    @Test
    @DisplayName("CT-35: delete deve lançar ResourceNotFoundException quando ID não existir")
    void ct35_delete_deveLancarExcecaoQuandoIdNaoExistir() {
        when(userRepository.findById("id-inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete("id-inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com o ID: id-inexistente");

        verify(userRepository, never()).delete(any());
    }
}