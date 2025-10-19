package org.example.testtaskforjavadev.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testtaskforjavadev.config.DataSourceProperties;
import org.example.testtaskforjavadev.config.DataSourcesConfiguration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceManager {
    
    private final DataSourcesConfiguration configuration;
    private final Map<String, DataSource> dataSourceCache = new HashMap<>();

    public DataSource getDataSource(DataSourceProperties properties) {
        return dataSourceCache.computeIfAbsent(
            properties.getName(), 
            name -> createDataSource(properties)
        );
    }

    public Map<String, DataSource> getAllDataSources() {
        Map<String, DataSource> dataSources = new HashMap<>();
        for (DataSourceProperties props : configuration.getDataSources()) {
            dataSources.put(props.getName(), getDataSource(props));
        }
        return dataSources;
    }

    private DataSource createDataSource(DataSourceProperties properties) {
        log.info("Creating DataSource for: {}", properties.getName());
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUser());
        config.setPassword(properties.getPassword());
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName(properties.getName() + "-pool");
        
        configureByStrategy(config, properties.getStrategy());
        
        return new HikariDataSource(config);
    }

    private void configureByStrategy(HikariConfig config, String strategy) {
        if (strategy == null) {
            return;
        }
        
        switch (strategy.toLowerCase()) {
            case "postgres":
                config.setDriverClassName("org.postgresql.Driver");
                break;
            case "mysql":
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.addDataSourceProperty("useSSL", "false");
                config.addDataSourceProperty("allowPublicKeyRetrieval", "true");
                break;
            case "oracle":
                config.setDriverClassName("oracle.jdbc.OracleDriver");
                break;
            default:
                log.warn("Unknown strategy: {}", strategy);
        }
    }

    public void closeAll() {
        dataSourceCache.values().forEach(ds -> {
            if (ds instanceof HikariDataSource) {
                ((HikariDataSource) ds).close();
            }
        });
        dataSourceCache.clear();
    }
}

