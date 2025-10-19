package org.example.testtaskforjavadev.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testtaskforjavadev.config.DataSourceProperties;
import org.example.testtaskforjavadev.config.DataSourcesConfiguration;
import org.example.testtaskforjavadev.datasource.DataSourceManager;
import org.example.testtaskforjavadev.dto.User;
import org.example.testtaskforjavadev.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAggregationService {

    private final DataSourcesConfiguration configuration;
    private final DataSourceManager dataSourceManager;
    private final UserRepository userRepository;

    public List<User> getAllUsers(String username, String name, String surname) {
        log.info("Filter params - username: {}, name: {}, surname: {}", username, name, surname);
        
        List<User> allUsers = new ArrayList<>();
        
        for (DataSourceProperties dataSourceProps : configuration.getDataSources()) {
            try {
                DataSource dataSource = dataSourceManager.getDataSource(dataSourceProps);
                List<User> users = userRepository.findAllUsers(dataSource, dataSourceProps);
                allUsers.addAll(users);
            } catch (Exception e) {
                log.error("Error fetching users from {}: {}", 
                    dataSourceProps.getName(), e.getMessage());
            }
        }
        
        List<User> filtered = filterUsers(allUsers, username, name, surname);
        log.info("Filtered {} users from {} total", filtered.size(), allUsers.size());
        return filtered;
    }

    private List<User> filterUsers(List<User> users, String username, String name, String surname) {
        return users.stream()
                .filter(user -> username == null || user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .filter(user -> name == null || user.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(user -> surname == null || user.getSurname().toLowerCase().contains(surname.toLowerCase()))
                .collect(Collectors.toList());
    }
}

