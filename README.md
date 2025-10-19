# User Data Aggregation Service

Spring Boot application for aggregating user data from multiple databases.

## Stack

- Java 17
- Spring Boot 3.5.6
- PostgreSQL, MySQL, Oracle
- Flyway
- Docker Compose
- Testcontainers

## Quick Start

### Prerequisites

- Docker & Docker Compose
- Maven 3.6+ (for local development)
- Java 17+ (for local development)

### Run with Docker Compose

```bash
docker-compose up --build -d
```

Access:
- API: `http://localhost:8080/users`
- Swagger: `http://localhost:8080/swagger-ui.html`


## API Documentation

### Get All Users

```http
GET /users
```

Returns aggregated user data from all configured databases.

**Response:**
```json
[
  {
    "id": 1,
    "username": "[postgres] ivan.petrenko",
    "name": "Ivan",
    "surname": "Petrenko"
  },
  {
    "id": 1,
    "username": "[mysql] taras.shevchenko",
    "name": "Taras",
    "surname": "Shevchenko"
  }
]
```

### Filtering

```bash
curl "http://localhost:8080/users?name=Ivan"
curl "http://localhost:8080/users?surname=Petrenko"
curl "http://localhost:8080/users?username=ivan"
```

Supports: `username`, `name`, `surname` (case-insensitive)

## Configuration

Data sources configured in `application.properties` with flexible column mapping.

## Testing

```bash
./mvnw test
```

Uses Testcontainers for integration tests.
