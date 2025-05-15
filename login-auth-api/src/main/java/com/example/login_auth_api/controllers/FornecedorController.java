package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.fornecedor.Fornecedor;
import com.example.login_auth_api.domain.produto.Produto;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.FornecedorRequestDTO;
import com.example.login_auth_api.dto.ProductRequestDTO;
import com.example.login_auth_api.service.FornecedorService;
import com.example.login_auth_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private ProductService produtoService;

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<String> getFornecedor(){
        return ResponseEntity.ok("Fornecedor!!");
    }

    @PreAuthorize("hasRole('FORNECEDOR')")
    @PostMapping("/criar-produtos")
    public ResponseEntity<Produto> criarProduto(@RequestBody ProductRequestDTO dto) {

        Produto novoProduto = produtoService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Fornecedor> registrarFornecedor(@RequestBody @Valid FornecedorRequestDTO dto) {
        Fornecedor novo = fornecedorService.criarFornecedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

}
