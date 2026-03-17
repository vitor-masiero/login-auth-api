package com.example.login_auth_api.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserRole {
    @JsonProperty("USER") ROLE_USER,
    @JsonProperty("ADMIN") ROLE_ADMIN
}
