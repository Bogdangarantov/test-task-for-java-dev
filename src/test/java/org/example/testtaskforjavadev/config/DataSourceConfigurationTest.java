package org.example.testtaskforjavadev.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class DataSourceConfigurationTest {

    @Autowired
    private DataSourcesConfiguration configuration;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("data-sources[0].name", () -> "test-source");
        registry.add("data-sources[0].strategy", () -> "postgres");
        registry.add("data-sources[0].url", postgres::getJdbcUrl);
        registry.add("data-sources[0].table", () -> "test_table");
        registry.add("data-sources[0].user", () -> "testuser");
        registry.add("data-sources[0].password", () -> "testpass");
        registry.add("data-sources[0].mapping.id", () -> "id");
        registry.add("data-sources[0].mapping.username", () -> "username");
        registry.add("data-sources[0].mapping.name", () -> "name");
        registry.add("data-sources[0].mapping.surname", () -> "surname");
    }

    @Test
    void shouldLoadDataSourcesConfiguration() {
        assertThat(configuration).isNotNull();
        assertThat(configuration.getDataSources()).isNotEmpty();
        assertThat(configuration.getDataSources()).hasSize(1);
    }

    @Test
    void shouldLoadDataSourceProperties() {
        DataSourceProperties props = configuration.getDataSources().get(0);

        assertThat(props.getName()).isEqualTo("test-source");
        assertThat(props.getStrategy()).isEqualTo("postgres");
        assertThat(props.getTable()).isEqualTo("test_table");
        assertThat(props.getUser()).isEqualTo("testuser");
        assertThat(props.getPassword()).isEqualTo("testpass");
    }

    @Test
    void shouldLoadMappingProperties() {
        MappingProperties mapping = configuration.getDataSources().get(0).getMapping();

        assertThat(mapping).isNotNull();
        assertThat(mapping.getId()).isEqualTo("id");
        assertThat(mapping.getUsername()).isEqualTo("username");
        assertThat(mapping.getName()).isEqualTo("name");
        assertThat(mapping.getSurname()).isEqualTo("surname");
    }
}

