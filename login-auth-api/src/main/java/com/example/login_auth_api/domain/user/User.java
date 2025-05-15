package com.example.login_auth_api.domain.user;
import com.example.login_auth_api.domain.endereco.Endereco;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(unique = true)
    private String dsEmail;
    private String nmUsuario;
    private String dsSenha;
    private String nuCnpjCpf;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> endereco;

    @Enumerated(EnumType.STRING)
    private UserRole enRole;

    public User(String nmUsuario, String dsEmail, String dsSenha, UserRole enRole, String nuCnpjCpf) {
        this.nmUsuario = nmUsuario;
        this.dsEmail = dsEmail;
        this.dsSenha = dsSenha;
        this.enRole = enRole;
        this.nuCnpjCpf = nuCnpjCpf;
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