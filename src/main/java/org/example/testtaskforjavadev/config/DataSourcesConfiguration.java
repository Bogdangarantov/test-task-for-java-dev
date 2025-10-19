package org.example.testtaskforjavadev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties
public class DataSourcesConfiguration {
    private List<DataSourceProperties> dataSources = new ArrayList<>();
}

