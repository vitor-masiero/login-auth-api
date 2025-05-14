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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tbusuario")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idUsuario;
    private String dsEmail;
    private String nmUsuario;
    private String dsSenha;
    private String nuCnpjCpf;

    @Enumerated(EnumType.STRING)
    private UserRole enRole;

    public User(String nm_usuario, String ds_email, String ds_senha_hash, UserRole enRole) {
        this.nmUsuario = nm_usuario;
        this.dsEmail = ds_email;
        this.dsSenha = ds_senha_hash;
        this.enRole = enRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.enRole == UserRole.FORNECEDOR) return List.of(new SimpleGrantedAuthority("ROLE_FORNECEDOR"));
        else return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));

    }

    @Override
    public String getPassword() {
        return dsSenha;
    }

    @Override
    public String getUsername() {
        return dsEmail;
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