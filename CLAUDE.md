# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build
./mvnw clean package
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=AuthServiceLoginTest

# Run a specific test method
./mvnw test -Dtest=AuthServiceLoginTest#testLoginSuccess

# Generate coverage report (output: target/site/jacoco/index.html)
./mvnw jacoco:report

# Docker (recommended for full stack)
docker compose up --build
```

## Architecture

Spring Boot 3 REST API for JWT-based authentication and authorization, running on port **8084**.

**Technology stack:** Java 21, Spring Boot 3, Spring Security, Spring Data JPA, PostgreSQL (production), H2 (tests), Auth0 java-jwt 4.4.0.

**Layered structure:**
- `controllers/` — AuthController, UserController, AdminController
- `services/` — AuthService (login/register logic), UserService
- `repositories/` — UserRepository (Spring Data JPA)
- `domain/user/` — User entity, UserRole enum (ROLE_USER, ROLE_ADMIN)
- `dto/` — Request/Response DTOs
- `infra/security/` — SecurityConfig, SecurityFilter (JWT validation per-request), TokenService
- `infra/handlers/` — GlobalExceptionHandler (centralized error handling)
- `exceptions/` — Custom exceptions (InvalidUserException, ResourceNotFoundException)

**Key endpoints:**
- `POST /auth/register` and `POST /auth/login` — public
- `GET /user/me`, `PUT /user/me` — requires valid JWT
- `GET /admin/users`, `DELETE /admin/users/{id}` — requires ROLE_ADMIN

**Authentication flow:** SecurityFilter intercepts every request, validates the Bearer token via TokenService, and loads the UserDetails into the SecurityContext. SecurityConfig defines which routes are public vs. protected.

## Environment & Configuration

**Production** (`application.yaml`): PostgreSQL, configured via environment variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `JWT_SECRET_KEY`. JWT tokens expire after 24 hours.

**Tests** (`application-test.yaml`): H2 in-memory database with PostgreSQL compatibility mode, `create-drop` DDL. Docker Compose is disabled. JWT secret is `test-secret-key-unit-tests`.

The `.env` file in the project root holds dev defaults (e.g., `JWT_SECRET_KEY=chartreuse`).

## Test Structure

Tests are split into:
- **Unit tests** (`services/`, `infra/TokenServiceTest`) — use Mockito (`@Mock`, `@InjectMocks`)
- **Integration tests** (`controllers/`, `repositories/`, `infra/GlobalExceptionHandlerIntegrationTest`) — use `@SpringBootTest` with MockMvc and the H2 test profile

Integration tests use `@ActiveProfiles("test")` which activates `application-test.yaml` automatically.

## API Collection

Bruno API collection with pre-configured requests is in `bruno-collections/` with environment config in `environments/Env.yml`. Useful for manual testing of all endpoints.
