package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.request.user.UserUpdateDTO;
import com.example.login_auth_api.dto.response.UserResponseDTO;
import com.example.login_auth_api.exceptions.ResourceNotFoundException;
import com.example.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public void delete(String id) {
        User product = this.getUserEntity(id);
        userRepository.delete(product);
    }

    public UserResponseDTO buscarUserPorId(String id) {
        return mapperResponseUser(getUserEntity(id));
    }

    public UserResponseDTO updateUser(UserUpdateDTO dto, String id) {
        User user = getUserEntity(id);

        user.setName(dto.nome());
        user.setTelefone(dto.telefone());
        user.setEndereco(dto.endereco());

        userRepository.save(user);
        return mapperResponseUser(user);
    }

    //Métodos de suporte

    private User getUserEntity(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
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
