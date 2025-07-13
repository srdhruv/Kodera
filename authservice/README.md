# 🔐 Kodera Auth Service

A secure and modular **Spring Boot Authentication Service** built with:
- JWT-based stateless authentication
- BCrypt password hashing
- Role-based access control
- Refresh token support
- PostgreSQL database

---

## 🚀 Tech Stack

| Layer         | Tech                                      |
|---------------|-------------------------------------------|
| Backend       | Spring Boot 3.x                           |
| Security      | Spring Security + JWT (JJWT 0.12.x)       |
| DB            | PostgreSQL                                |
| Persistence   | Spring Data JPA                           |
| Token Storage | In-memory (refresh token in response only)|
| Password Hash | BCrypt                                    |

---

## 📦 Project Structure

```
com.kodera.authservice
├── controller        # REST endpoints
├── dto              # Request/response payloads
├── entity           # JPA entities
├── repository       # Spring Data Repositories
├── security         # JWT filters and config
├── service          # Business logic
├── config           # Properties and beans
└── KoderaAuthServiceApplication.java
```

---

## 🔐 Security Features

### ✅ Password Hashing
- Implemented using `BCryptPasswordEncoder`
- Hashes passwords before saving to DB

### ✅ JWT Authentication
- JWT Access Token contains `sub`, `iat`, `exp`, and `role`
- Secret key and expiration time loaded from `application.yml`

### ✅ Role-Based Access Control
- `User` entity has a `Role` enum
- Roles are injected as JWT claims and validated in `SecurityFilterChain`

### ✅ Refresh Token Support
- Separate endpoint to renew access token
- Refresh token is returned at login and must be sent via header for refresh

---

## 🗄️ Database

### ✅ PostgreSQL + JPA
Configured in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

```

### ✅ Entity Schema

#### `User`

| Field     | Type     | Notes                |
|-----------|----------|----------------------|
| id        | UUID     | Primary Key          |
| email     | String   | Unique, login ID     |
| password  | String   | BCrypt encoded       |
| role      | Enum     | `USER`, `ADMIN`, etc |

---

## 🧪 Auth API Endpoints

### 📝 Register User

```
POST /auth/register
```

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securepass",
  "role": "USER"
}
```

**Response:**
```
200 OK
User Registered Successfully
```

---

### 🔐 Login

```
POST /auth/login
```

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securepass"
}
```

**Response:**
```json
{
  "accessToken": "jwt_token_here",
  "refreshToken": "refresh_token_here"
}
```

---

### ♻️ Refresh Token

```
POST /auth/refresh
```

**Header:**
```
Authorization: Bearer <refresh_token>
```

**Response:**
```json
{
  "accessToken": "new_jwt_token",
  "refreshToken": "new_refresh_token"
}
```

---

### 🔐 Secured Endpoint Example

```
GET /hello
```

**Header:**
```
Authorization: Bearer <access_token>
```

Only accessible if token is valid.

---

## 🔧 JWT Configuration

In `application.yml`:
```yaml
jwt:
  secret: ${JWT_SECRET}
  access-token-expiration-ms: 3600000       # 1 hour
  refresh-token-expiration-ms: 86400000    # 2 days
```

In `JwtService`:
- Tokens built with:
  ```java
  Jwts.builder()
    .claims(extraClaims)
    .subject(subject)
    .issuedAt(now)
    .expiration(expiry)
    .signWith(secretKey, HS256)
    .compact();
  ```

---

## 🔐 Spring Security Setup

In `SecurityConfig`:
- `JwtAuthenticationFilter` runs before `UsernamePasswordAuthenticationFilter`
- Routes like `/auth/**` are publicly accessible
- All other routes require JWT

---

## 📌 Notes

- Currently, refresh tokens are not stored — they’re trusted until expired
- You can extend this to store tokens and support token revocation later
- Passwords are never stored in plain text — only BCrypt hashes
- Access token carries `role` info and is validated on every request

---

## ✅ To Do / Extensions

- [ ] Store refresh tokens in DB
- [ ] Add method-level security via `@PreAuthorize`
- [ ] Add email verification / password reset
- [ ] Add test coverage (unit + integration)
- [ ] Frontend integration (React/HTML form)

---

## 📦 Running the Project

```bash
./mvnw spring-boot:run
```

Make sure PostgreSQL is running and matches credentials in `application.yml`.

---