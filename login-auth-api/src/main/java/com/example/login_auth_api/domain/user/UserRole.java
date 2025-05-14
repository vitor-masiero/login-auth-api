package com.example.login_auth_api.domain.user;

public enum UserRole {

    CLIENTE("cliente"),
    FORNECEDOR("fornecedor");

    private String enRole;

    UserRole(String role) {
        this.enRole = enRole;
    }

    public String getRole() {
        return enRole;
    }

}
