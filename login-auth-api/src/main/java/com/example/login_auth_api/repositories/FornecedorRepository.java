package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, String> {
    Optional<Fornecedor> findByDsEmail(String dsEmail);
}
