package org.example.testtaskforjavadev;

import org.example.testtaskforjavadev.config.DataSourcesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DataSourcesConfiguration.class)
public class TestTaskForJavaDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskForJavaDevApplication.class, args);
    }

}
