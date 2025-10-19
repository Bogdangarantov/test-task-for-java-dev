CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users (login, first_name, last_name)
VALUES ('ivan.petrenko', 'Ivan', 'Petrenko'),
    ('maria.koval', 'Maria', 'Koval'),
    ('oleksandr.sirko', 'Oleksandr', 'Sirko');