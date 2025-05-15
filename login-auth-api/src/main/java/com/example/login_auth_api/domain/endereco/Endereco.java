package com.example.login_auth_api.domain.endereco;
import com.example.login_auth_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TBENDERECO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dsLogradouro;
    private String nmNumero;
    private String dsComplemento;
    private Boolean endFavorito;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private User usuario;
}
