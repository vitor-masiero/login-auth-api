package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.request.auth.LoginRequestDTO;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import com.example.login_auth_api.dto.response.LoginResponseDTO;
import com.example.login_auth_api.dto.response.RegisterResponseDTO;
import com.example.login_auth_api.exceptions.InvalidUserException;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public RegisterResponseDTO registerUser(RegisterRequestDTO data) {
        this.userRepository.findByEmail(data.email())
                .ifPresent(user -> {
                    throw new InvalidUserException("Usuário já existe. Faça login!");
                });

        User newUser = new User(data);
        newUser.setPassword(passwordEncoder.encode(data.senha()));

        this.userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);
        return new RegisterResponseDTO(
                newUser.getName(),
                newUser.getEmail(),
                newUser.getTelefone(),
                newUser.getEndereco(),
                newUser.getRole(),
                token);
    }

    public LoginResponseDTO login(LoginRequestDTO data) {
        User user = this.userRepository.findByEmail(data.email())
                .orElseThrow(() -> new InvalidUserException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(data.senha(), user.getPassword())) {
            throw new InvalidUserException("Usuário ou senha inválidos");
        }

        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(user.getName(), user.getEmail(), token);
    }
}
