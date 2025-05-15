package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.fornecedor.Fornecedor;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.FornecedorRequestDTO;
import com.example.login_auth_api.repositories.FornecedorRepository;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FornecedorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional
    public Fornecedor criarFornecedor(FornecedorRequestDTO dto) {
        User user =  userRepository.findById(dto.idFornecedor());
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado com o e-mail: " + dto.idFornecedor());
        }

        Fornecedor fornecedor = new Fornecedor();
        // Herdado de User
        fornecedor.setDsEmail(user.getDsEmail());
        fornecedor.setDsSenha(user.getDsSenha());
        fornecedor.setEnRole(user.getEnRole());

        fornecedor.setDsRazaoSocial(dto.dsRazaoSocial());
        LocalDateTime horario = LocalDateTime.parse(dto.dtHorarioFunc(), FORMATTER);
        fornecedor.setDtHorarioFunc(horario);

        return fornecedorRepository.save(fornecedor);
    }
}
