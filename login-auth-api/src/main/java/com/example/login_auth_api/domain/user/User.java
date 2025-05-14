package com.example.login_auth_api.domain.user;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tbusuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idUsuario;
    private String dsEmail;
    private String nmUsuario;
    private String dsSenha;
    private String nuCnpjCpf;

    @Enumerated(EnumType.STRING)
    private UserRole en_role;

    public User(String nm_usuario, String ds_email, String ds_senha_hash, UserRole en_role) {
        this.nmUsuario = nm_usuario;
        this.dsEmail = ds_email;
        this.dsSenha = ds_senha_hash;
        this.en_role = en_role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.en_role == UserRole.FORNECEDOR) return List.of(new SimpleGrantedAuthority("ROLE_FORNECEDOR"));
        else return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));

    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}