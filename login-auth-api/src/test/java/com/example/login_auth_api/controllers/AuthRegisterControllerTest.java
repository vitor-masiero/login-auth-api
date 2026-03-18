package com.example.login_auth_api.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
public class AuthRegisterControllerTest {
}
