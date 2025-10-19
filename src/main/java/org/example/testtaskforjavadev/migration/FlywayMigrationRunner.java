package org.example.testtaskforjavadev.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testtaskforjavadev.config.DataSourceProperties;
import org.example.testtaskforjavadev.config.DataSourcesConfiguration;
import org.example.testtaskforjavadev.datasource.DataSourceManager;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlywayMigrationRunner {

    private final DataSourcesConfiguration configuration;
    private final DataSourceManager dataSourceManager;

    @EventListener(ApplicationReadyEvent.class)
    public void runMigrations() {
        log.info("Running migrations for {} data sources...", configuration.getDataSources().size());

        for (DataSourceProperties props : configuration.getDataSources()) {
            try {
                log.info("Migrating {}", props.getName());
                migrateDataSource(props);
                log.info("Successfully migrated {}", props.getName());
            } catch (Exception e) {
                log.error("Migration failed for {}", props.getName(), e);
                throw new RuntimeException("Failed to migrate " + props.getName(), e);
            }
        }
        
        log.info("All migrations completed!");
    }

    private void migrateDataSource(DataSourceProperties props) {
        DataSource dataSource = dataSourceManager.getDataSource(props);
        String migrationLocation = getMigrationLocation(props.getStrategy());

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(migrationLocation)
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .load();

        flyway.migrate();
    }

    private String getMigrationLocation(String strategy) {
        if (strategy == null) {
            return "classpath:db/migration/common";
        }

        return switch (strategy.toLowerCase()) {
            case "postgres" -> "classpath:db/migration/postgres";
            case "mysql" -> "classpath:db/migration/mysql";
            case "oracle" -> "classpath:db/migration/oracle";
            default -> "classpath:db/migration/common";
        };
    }
}

