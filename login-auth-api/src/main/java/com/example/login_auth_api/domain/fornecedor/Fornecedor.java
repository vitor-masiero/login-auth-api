package com.example.login_auth_api.domain.fornecedor;

import com.example.login_auth_api.domain.produto.Produto;
import com.example.login_auth_api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbfornecedor")
@Getter
@Setter

public class Fornecedor extends User{

    private String dsRazaoSocial;
    private LocalDateTime dtHorarioFunc;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produto;

}
