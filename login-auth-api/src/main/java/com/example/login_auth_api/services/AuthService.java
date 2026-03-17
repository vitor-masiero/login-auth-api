package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import com.example.login_auth_api.dto.response.RegisterResponseDTO;
import com.example.login_auth_api.dto.response.UserResponseDTO;
import com.example.login_auth_api.exceptions.ResourceNotFoundException;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Page<UserResponseDTO> listarUsuariosComPaginacao(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        return usersPage.map(p -> new UserResponseDTO(
                p.getId(),
                p.getName(),
                p.getEmail(),
                p.getTelefone(),
                p.getEndereco(),
                p.getRole()
        ));
    }

    //Método privado para retornar apenas a entidade do usuário.
    private User getUserEntity(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O usuário não está disponível ou não foi encontrado."));
    }

    //Método público de uso externo no controller
    public UserResponseDTO buscarUserPorId(String id) {
        User user = getUserEntity(id);
        return mapperResponseUser(user);
    }

    public UserResponseDTO registrarUsuario(RegisterRequestDTO body) {
        Optional<User> user = this.userRepository.findByEmail(body.email());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.senha()));
            newUser.setEmail(body.email());
            newUser.setName(body.nome());
            newUser.setRole(body.role());
            this.userRepository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return mapperResponseUser(user);

        }
        return mapperResponseUser(savedProduct);
    }

    public UserResponseDTO updateProduct(UserResponseDTO dto, String id) {
        User produto = this.getUserEntity(id);
        BeanUtils.copyProperties(dto, produto);
        userRepository.save(produto);
        return mapperResponseUser(produto);
    }

    public void deleteProduct(String id) {
        User product = this.getUserEntity(id);
        userRepository.delete(product);
    }

    private UserResponseDTO mapperResponseUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getTelefone(),
                user.getEndereco(),
                user.getRole()
        );
    }
}
