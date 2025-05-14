package com.example.login_auth_api.controllers;

import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.service.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import com.example.login_auth_api.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')") - Anotação para cargos dentro do sistema

public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO body) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(body.dsEmail(), body.dsSenha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        User user  = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(
                new ResponseDTO(token, user.getNmUsuario(), user.getEnRole())
        );

        //User user = repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        //if(passwordEncoder.matches(body.password(), user.getPassword())) {
           // UserRole role = body.role();
            //String token = tokenService.generateToken(user);
            //return ResponseEntity.ok(new ResponseDTO(user.getName(), token, role));
        //}
        //return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO body) {
        if(this.repository.findByDsEmail(body.dsEmail()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(body.dsSenha());
        User newUser = new User(body.nmUsuario(), body.dsEmail(), encryptedPassword, body.enRole());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();

        //Optional<User> user = this.repository.findByEmail(body.email());
        //if(user.isEmpty()){
            //User newUser = new User();
            //newUser.setPassword(passwordEncoder.encode(body.password()));
            //newUser.setEmail(body.email());
            //newUser.setName(body.name());
            //newUser.setRole(body.role());
            //this.repository.save(newUser);

            //String token = tokenService.generateToken(newUser);
            //return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token, newUser.getRole()));

        //}
        //return ResponseEntity.badRequest().build();
    }
}
