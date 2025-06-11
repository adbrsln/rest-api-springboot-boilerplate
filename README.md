# Spring Boot REST API Boilerplate

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)
![Maven](https://img.shields.io/badge/build-maven-red.svg)
![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)

This repository serves as a comprehensive and production-ready boilerplate for building robust, secure, and scalable RESTful APIs using the Spring Boot framework. It incorporates a wide range of best practices and modern tools to accelerate development.

## ✨ Core Features

This boilerplate comes pre-configured with the following features:

-   🔐 **JWT Security:** End-to-end security using JSON Web Tokens (JWT) for authentication and authorization.
-   👤 **Role-Based Access Control (RBAC):** Pre-configured with `USER` and `ADMIN` roles.
-   📖 **API Documentation:** Interactive API documentation with Swagger UI and OpenAPI 3.
-   🗄️ **JPA + Hibernate:** A complete data persistence layer using Spring Data JPA.
-   🔄 **DTO Pattern with MapStruct:** Clean separation between database entities and API models, with boilerplate-free mapping.
-   ⚙️ **Centralized Exception Handling:** Graceful and consistent error responses for a professional API experience.
-   ⚡ **Caching with Caffeine:** High-performance in-memory caching to boost API response times.
-   🔀 **Database Support:** Configured for both H2 (for development) and PostgreSQL (for production).
-   핫 **Developer Tools:** Pre-configured with Spring Boot DevTools for automatic application restarts.
-   ✔️ **Input Validation:** Built-in validation for API request bodies.

## 🛠️ Tech Stack

-   **Framework:** Spring Boot 3.x
-   **Security:** Spring Security, JSON Web Tokens (JWT)
-   **Database:** Spring Data JPA, Hibernate, H2, PostgreSQL
-   **API Docs:** Springdoc OpenAPI
-   **Mapping:** MapStruct
-   **Caching:** Spring Cache, Caffeine
    -.  **Build:** Apache Maven

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

-   JDK 17 or later
-   Apache Maven
-   An IDE like IntelliJ IDEA, VS Code, or Eclipse

### 1. Clone the Repository

```bash
git clone https://github.com/adbrsln/rest-api-springboot-boilerplate.git
cd rest-api-springboot-boilerplate
```

### 2. Configure the Application

Open the `src/main/resources/application.properties` file.

#### A. Configure the JWT Secret Key

This is the most important security configuration. **Never use the default key in production.**

```properties
# A strong, base64-encoded secret key for signing JWTs.
# Generate one from a secure source.
application.security.jwt.secret-key=your-super-strong-base64-encoded-secret-key-here
```
**For production, it is highly recommended to set this as an environment variable:**
`export JWT_SECRET_KEY=your-secret-key` and reference it as `application.security.jwt.secret-key=${JWT_SECRET_KEY}`.

#### B. Configure the Database

The project is pre-configured to use the H2 in-memory database, which requires no setup.

To switch to **PostgreSQL** for production, comment out the H2 section and uncomment the PostgreSQL section:

```properties
# --- H2 (Development) ---
# spring.datasource.url=jdbc:h2:mem:testdb
# ... (rest of H2 config)

# --- PostgreSQL (Production) ---
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Run the Application

You can run the application using the Maven wrapper:

```bash
# On Mac/Linux
./mvnw spring-boot:run

# On Windows
./mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`.

## 📚 API Documentation & Usage

Once the application is running, you can explore and interact with the API using Swagger UI.

-   **Swagger UI URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
-   **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:testdb`, User: `sa`, no password)

### Authentication Flow

1.  **Register a new user:**
    -   Use the `POST /api/v1/auth/register` endpoint.
2.  **Authenticate to get a token:**
    -   Use the `POST /api/v1/auth/authenticate` endpoint with the credentials you just registered.
    -   The response will contain an `access_token`.
3.  **Access protected endpoints:**
    -   Click the "Authorize" button in Swagger UI and paste your token in the format `Bearer <your_token>`.
    -   Now you can make requests to protected endpoints like `GET /api/v1/users`.

### Key Endpoints

| Method | Endpoint                    | Access  | Description                                  |
| :----- |:----------------------------| :------ |:---------------------------------------------|
| `POST` | `/api/v1/auth/register`     | Public  | Register a new user account.                 |
| `POST` | `/api/v1/auth/authenticate` | Public  | Authenticate and receive a JWT access token. |
| `GET`  | `/api/v1/users`             | `ADMIN` | Get a paginated list of all users.           |
| `GET`  | `/api/v1/users/{id}`        | `USER`  | Get details for a specific user.             |
| `PUT`  | `/api/v1/users/{id}`        | `USER`  | Update a user's details.                     |
| `GET`  | `/api/v1/todos`             | `USER`  | Get todos listing                            |
| `GET`  | `/api/v1/todos/{id}`        | `USER`  | Get todos specific todos                     |

### Example `cURL` Request

```bash

# 1. Register sample user
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/authenticate \
-H "Content-Type: application/json" \
-d '{"username": "user", "email": "user@example.com", "password": "password"}' | jq -r '.access_token')

# 1. Authenticate to get a token
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/authenticate \
-H "Content-Type: application/json" \
-d '{"username": "user", "password": "password"}' | jq -r '.access_token')

# 2. Use the token to access a protected endpoint
curl -X GET http://localhost:8080/api/v1/users/1 \
-H "Authorization: Bearer $TOKEN"
```

## 📂 Project Structure

The project follows a standard layered architecture to ensure separation of concerns.

```
src/main/java/com/adbrsln/boilerplate/
├── config/             # Spring configuration (Security, OpenAPI, Caching)
├── controller/         # REST API controllers (entry points)
├── dto/                # Data Transfer Objects (for API requests/responses)
├── entity/             # JPA entities (database models)
├── exception/          # Custom exceptions and global handler
├── mapper/             # MapStruct mappers for DTO-Entity conversion
├── repository/         # Spring Data JPA repositories (data access layer)
├── security/           # JWT filter, service, and security configurations
└── service/            # Business logic layer
```

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.