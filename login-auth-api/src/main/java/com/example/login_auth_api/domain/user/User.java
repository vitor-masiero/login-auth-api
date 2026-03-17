package com.example.login_auth_api.domain.user;

import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String name;
    private String telefone;
    private String password;
    private String endereco;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    //Construtor para evitar boilerplate no Service
    public User(RegisterRequestDTO data) {
        this.name = data.nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.endereco = data.endereco();
        this.password = data.senha();
        this.role = data.role();
    }
}
