CREATE TABLE user_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usr_login VARCHAR(100) NOT NULL UNIQUE,
    usr_name VARCHAR(100),
    usr_surname VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO user_table (usr_login, usr_name, usr_surname)
VALUES ('taras.shevchenko', 'Taras', 'Shevchenko'),
    ('olena.bondar', 'Olena', 'Bondar'),
    (
        'dmytro.vyshnevetskyi',
        'Dmytro',
        'Vyshnevetskyi'
    );