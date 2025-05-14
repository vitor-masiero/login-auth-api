package com.example.login_auth_api.domain.fornecedor;

import com.example.login_auth_api.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbfornecedor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Fornecedor extends User {
    private String dsRazaoSocial;
    private String dsCategoria;
    private LocalDateTime dtHrFuncionamento;
    private Double vlMinimoPedido;
}
