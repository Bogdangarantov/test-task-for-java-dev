package org.example.testtaskforjavadev.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testtaskforjavadev.config.DataSourceProperties;
import org.example.testtaskforjavadev.config.MappingProperties;
import org.example.testtaskforjavadev.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    public List<User> findAllUsers(DataSource dataSource, DataSourceProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        MappingProperties mapping = properties.getMapping();
        
        String query = buildQuery(properties.getTable(), mapping);
        String sourcePrefix = properties.getName().split("-")[0];
        
        return jdbcTemplate.query(query, (rs, rowNum) -> new User(
            rs.getLong("id"),
            "[" + sourcePrefix + "] " + rs.getString("username"),
            rs.getString("name"),
            rs.getString("surname")
        ));
    }

    private String buildQuery(String table, MappingProperties mapping) {
        return String.format(
            "SELECT %s as id, %s as username, %s as name, %s as surname FROM %s",
            mapping.getId(),
            mapping.getUsername(),
            mapping.getName(),
            mapping.getSurname(),
            table
        );
    }
}

