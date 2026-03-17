package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.request.auth.RegisterRequestDTO;
import com.example.login_auth_api.dto.request.user.UserUpdateDTO;
import com.example.login_auth_api.dto.response.UserResponseDTO;
import com.example.login_auth_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getPerfil() {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponseDTO response = userService.buscarUserPorId(userLogado.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updatePerfil(@RequestBody @Valid UserUpdateDTO data) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponseDTO updated = userService.updateUser(data, user.getId());
        return ResponseEntity.ok(updated);
    }
}
