package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

}
