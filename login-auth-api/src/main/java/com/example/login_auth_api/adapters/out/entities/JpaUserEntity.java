package com.example.login_auth_api.adapters.out.entities;
import com.example.login_auth_api.domain.user.UserRole;
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

public class JpaUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
