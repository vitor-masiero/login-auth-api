# login-auth-api

API REST de autenticação e autorização construída com Spring Boot 3, JWT e PostgreSQL. Oferece cadastro de usuários, login com geração de token JWT e controle de acesso por roles (`ROLE_USER` e `ROLE_ADMIN`).

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Security | — |
| Spring Data JPA | — |
| PostgreSQL | 16 |
| JWT (Auth0) | 4.4.0 |
| H2 (testes) | — |
| Docker / Docker Compose | — |
| Maven | 3.9.x |

---

## Pré-requisitos

Antes de iniciar, garanta que os itens abaixo estão instalados:

- [JDK 21](https://adoptium.net/)
- [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/)
- [Git](https://git-scm.com/)

---

## Instalação

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd login-auth-api
```

---

### 2. Configurar as variáveis de ambiente

Crie o arquivo `.env` na raiz do projeto com o seguinte conteúdo:

```env
JWT_SECRET_KEY=sua_chave_secreta_aqui
```

> A chave JWT pode ser qualquer string.
> O arquivo `.env` não deve ser commitado — adicione-o ao `.gitignore`.

---

### 3. Escolha a forma de execução

---

#### Opção A — Docker Compose (recomendado)

Sobe a API e o banco PostgreSQL juntos em containers, sem precisar instalar o banco localmente.

```bash
docker compose up --build
```

A API estará disponível em: `http://localhost:8084`

Para encerrar:

```bash
docker compose down
```

Para encerrar e remover os volumes do banco:

```bash
docker compose down -v
```

---

#### Opção B — Execução local (sem Docker)

Requer um PostgreSQL rodando localmente com as seguintes configurações:

| Parâmetro | Valor |
|---|---|
| Host | `localhost` |
| Porta | `5432` |
| Banco | `jwtauth` |
| Usuário | `postgres` |
| Senha | `root` |

Exporte as variáveis de ambiente antes de rodar:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/jwtauth
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=root
export JWT_SECRET_KEY=sua_chave_secreta_aqui
```

Em seguida, suba a aplicação:

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8084`

---

## Testando a API com Bruno

O projeto inclui uma collection completa para o [Bruno API Client](https://www.usebruno.com/), localizada na pasta `Login-Auth-API/` na raiz do repositório.

### Como importar

1. Abra o Bruno
2. Clique em Workspaces
3. Open Workspace
4. Selecione a pasta `login-auth-api/bruno-collections`
5. Configure o Environment

### Requisições disponíveis

| Pasta | Requisição |
|---|---|
| **Auth** | Register - USER |
| **Auth** | Register - ADMIN |
| **Auth** | Login - USER |
| **Auth** | Login - ADMIN |
| **User** | Ver dados do Perfil |
| **User** | Update dados comuns |
| **Admin** | Listar usuários |
| **Admin** | Listar usuários - Paginação |
| **Admin** | Excluir Usuário |

### Configurar o ambiente

A collection possui um ambiente pré-configurado em `environments/Env.yml`.
Selecione o ambiente **Env** no Bruno antes de executar as requisições.

---

## Testes Automatizados

Os testes utilizam banco H2 em memória e não precisam de Docker ou PostgreSQL.

### Rodar todos os testes

```bash
./mvnw test
```

### Rodar um teste específico

```bash
./mvnw test -Dtest=NomeDaClasseTest
```

### Gerar relatório de cobertura (JaCoCo)

```bash
Ss
```

O relatório é gerado em:

```
target/site/jacoco/index.html
```

---

## Endpoints

### Autenticação — públicos

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastro de novo usuário |
| `POST` | `/auth/login` | Login e geração de token JWT |

### Usuário — requer token (`ROLE_USER` ou `ROLE_ADMIN`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/user/me` | Retorna dados do usuário autenticado |
| `PUT` | `/user/me` | Atualiza dados do usuário autenticado |

### Admin — requer token (`ROLE_ADMIN`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/admin/users` | Lista todos os usuários |
| `DELETE` | `/admin/users/{id}` | Remove um usuário pelo ID |

---

## Autenticação

A API utiliza JWT stateless. Para acessar endpoints protegidos, inclua o token no header:

```
Authorization: Bearer <token>
```

O token é retornado no corpo da resposta do `/auth/login`.

---

## Exemplos de requisição

### Cadastro

```bash
curl -X POST http://localhost:8084/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "11987654321",
    "endereco": "Rua das Flores, 10",
    "senha": "Senha@123",
    "role": "ROLE_USER"
  }'
```

### Login

```bash
curl -X POST http://localhost:8084/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "Senha@123"
  }'
```

### Acessar perfil (com token)

```bash
curl http://localhost:8084/user/me \
  -H "Authorization: Bearer <token>"
```

---

## Estrutura do projeto

```
src/
├── main/java/com/example/login_auth_api/
│   ├── controllers/        # AuthController, UserController, AdminController
│   ├── domain/user/        # Entidade User e enum UserRole
│   ├── dto/                # DTOs de request e response
│   ├── exceptions/         # Exceções de domínio
│   ├── infra/
│   │   ├── handlers/       # GlobalExceptionHandler
│   │   └── security/       # SecurityConfig, SecurityFilter, TokenService
│   ├── repositories/       # UserRepository
│   └── services/           # AuthService, UserService
└── test/java/com/example/login_auth_api/
    ├── controllers/        # Testes de integração dos controllers
    ├── infra/              # Testes de integração da infraestrutura
    ├── repositories/       # Testes de integração do repositório
    └── services/           # Testes unitários dos services
```

---

## Variáveis de ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexão com o banco | `jdbc:postgresql://localhost:5432/jwtauth` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `root` |
| `JWT_SECRET_KEY` | Chave secreta para assinar o JWT | `minha-chave-secreta` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estratégia do Hibernate (opcional) | `update` |
