package com.example.login_auth_api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TokenServiceTest {

    @Autowired
    private TokenService tokenService;
    private User usuario;

    private static final String SECRET = "test-secret-key";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);

        usuario = new User();
        usuario.setEmail("joao@email.com");
        usuario.setName("João Silva");
        usuario.setRole(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("CT-13: generateToken deve retornar um token não nulo e não vazio")
    void ct13_deveGerarTokenNaoNuloENaoVazio() {
        String token = tokenService.generateToken(usuario);

        assertThat(token)
                .isNotNull()
                .isNotBlank();
    }

    @Test
    @DisplayName("CT-14: Token gerado deve conter o email do usuário como subject")
    void ct14_tokenDeveConterEmailComoSubject() {
        String token = tokenService.generateToken(usuario);

        String subject = JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();

        assertThat(subject).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("CT-15: Token gerado deve conter o issuer 'login-auth-api'")
    void ct15_tokenDeveConterIssuerCorreto() {
        String token = tokenService.generateToken(usuario);

        String issuer = JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getIssuer();

        assertThat(issuer).isEqualTo("login-auth-api");
    }

    @Test
    @DisplayName("CT-16: Token gerado deve expirar aproximadamente 2 horas no futuro")
    void ct16_tokenDeveExpirarEm2Horas() {
        String token = tokenService.generateToken(usuario);

        Instant expiracao = JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getExpiresAtAsInstant();

        Instant esperado = LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-3"));

        assertThat(expiracao).isCloseTo(esperado, within(10, java.time.temporal.ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("CT-17: verifyToken com token válido deve retornar o email correto")
    void ct17_verifyTokenValidoDeveRetornarEmail() {
        String token = tokenService.generateToken(usuario);

        String email = tokenService.verifyToken(token);

        assertThat(email).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("CT-18: verifyToken com token assinado com chave errada deve retornar null")
    void ct18_verifyTokenComChaveErradaDeveRetornarNull() {
        String tokenComChaveErrada = JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("joao@email.com")
                .withExpiresAt(Instant.now().plusSeconds(7200))
                .sign(Algorithm.HMAC256("chave-errada-qualquer"));

        String resultado = tokenService.verifyToken(tokenComChaveErrada);

        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("CT-19: verifyToken com token malformado deve retornar null")
    void ct19_verifyTokenMalformadoDeveRetornarNull() {
        String resultado = tokenService.verifyToken("isso.nao.e.um.jwt");

        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("CT-20: verifyToken com token expirado deve retornar null")
    void ct20_verifyTokenExpiradoDeveRetornarNull() {
        String tokenExpirado = JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("joao@email.com")
                .withExpiresAt(Instant.now().minusSeconds(3600)) // 1 hora no passado
                .sign(Algorithm.HMAC256(SECRET));

        String resultado = tokenService.verifyToken(tokenExpirado);

        assertThat(resultado).isNull();
    }
}