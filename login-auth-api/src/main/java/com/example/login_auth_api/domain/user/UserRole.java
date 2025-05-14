package com.example.login_auth_api.domain.user;

public enum UserRole {

    CLIENTE("cliente"),
    FORNECEDOR("fornecedor");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
