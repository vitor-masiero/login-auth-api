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

public class Fornecedor extends User{
}
