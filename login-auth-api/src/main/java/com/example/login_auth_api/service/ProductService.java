package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.fornecedor.Fornecedor;
import com.example.login_auth_api.domain.produto.Produto;
import com.example.login_auth_api.dto.ProductRequestDTO;
import com.example.login_auth_api.repositories.FornecedorRepository;
import com.example.login_auth_api.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Produto criarProduto(ProductRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findByDsEmail(dto.dsEmail())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com o e-mail: " + dto.dsEmail()));

        Produto produto = new Produto();
        produto.setNmProduto(dto.nmProduto());
        produto.setVlProduto(dto.vlProduto());
        produto.setDsProduto(dto.dsProduto());
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }
}
