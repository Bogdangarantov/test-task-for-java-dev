package org.example.testtaskforjavadev.config;

import lombok.Data;

@Data
public class DataSourceProperties {
    private String name;
    private String strategy;
    private String url;
    private String table;
    private String user;
    private String password;
    private MappingProperties mapping;
}

