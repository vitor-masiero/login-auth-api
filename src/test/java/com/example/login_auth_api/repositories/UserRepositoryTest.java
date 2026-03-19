package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar usuário cadastrado no DB")
    void findByEmailRetornaUsuarioCadastrado() {
        String email = "jose@gmail.com";
        RegisterRequestDTO data = new RegisterRequestDTO("José", email, "48999064470", "Rua das Flores", "senhasegura123", UserRole.ROLE_USER);
        this.createUser(data);

        Optional<User> foundedUser = this.userRepository.findByEmail(email);
        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não deve retornar usuário do DB quando não existe")
    void findByEmailNaoRetornaQuandoNaoExiste() {
        String email = "jose@gmail.com";
        Optional<User> foundedUser = this.userRepository.findByEmail(email);
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(RegisterRequestDTO data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}
