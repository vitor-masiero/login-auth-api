package com.example.login_auth_api.controllers;

import com.example.login_auth_api.dto.response.UserResponseDTO;
import com.example.login_auth_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

        private final UserService userService;

        @GetMapping("/users")
        public ResponseEntity<List<UserResponseDTO>> listAllUsers(Pageable pageable) {
            return ResponseEntity.ok(userService.listarUsuariosComPaginacao(pageable).getContent());
        }

        @DeleteMapping("/users/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable String id) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }

