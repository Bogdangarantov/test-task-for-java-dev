package org.example.testtaskforjavadev.integration;

import org.example.testtaskforjavadev.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserAggregationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("data-sources[0].name", () -> "postgres-test");
        registry.add("data-sources[0].strategy", () -> "postgres");
        registry.add("data-sources[0].url", postgres::getJdbcUrl);
        registry.add("data-sources[0].table", () -> "users");
        registry.add("data-sources[0].user", postgres::getUsername);
        registry.add("data-sources[0].password", postgres::getPassword);
        registry.add("data-sources[0].mapping.id", () -> "id");
        registry.add("data-sources[0].mapping.username", () -> "login");
        registry.add("data-sources[0].mapping.name", () -> "first_name");
        registry.add("data-sources[0].mapping.surname", () -> "last_name");

        // MySQL
        registry.add("data-sources[1].name", () -> "mysql-test");
        registry.add("data-sources[1].strategy", () -> "mysql");
        registry.add("data-sources[1].url", mysql::getJdbcUrl);
        registry.add("data-sources[1].table", () -> "user_table");
        registry.add("data-sources[1].user", mysql::getUsername);
        registry.add("data-sources[1].password", mysql::getPassword);
        registry.add("data-sources[1].mapping.id", () -> "id");
        registry.add("data-sources[1].mapping.username", () -> "usr_login");
        registry.add("data-sources[1].mapping.name", () -> "usr_name");
        registry.add("data-sources[1].mapping.surname", () -> "usr_surname");
    }

    @Test
    void shouldAggregateUsersFromMultipleDatabases() {
        String url = "http://localhost:" + port + "/users";
        ResponseEntity<List<User>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        List<User> users = response.getBody();
        
        assertThat(users).hasSize(6);
        assertThat(users).allMatch(user -> 
            user.getId() != null &&
            user.getUsername() != null &&
            user.getName() != null &&
            user.getSurname() != null
        );
        
        assertThat(users).anyMatch(user -> 
            user.getUsername().equals("ivan.petrenko")
        );
        
        assertThat(users).anyMatch(user -> 
            user.getUsername().equals("taras.shevchenko")
        );
    }

    @Test
    void shouldReturnValidUserStructure() {
        String url = "http://localhost:" + port + "/users";
        ResponseEntity<List<User>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> users = response.getBody();
        assertThat(users).isNotEmpty();

        User firstUser = users.get(0);
        assertThat(firstUser).hasNoNullFieldsOrProperties();
        assertThat(firstUser.getId()).isNotNull();
        assertThat(firstUser.getUsername()).isNotBlank();
        assertThat(firstUser.getName()).isNotBlank();
        assertThat(firstUser.getSurname()).isNotBlank();
    }
}

