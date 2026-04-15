CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    provider VARCHAR(50),
    role VARCHAR(50),
    created_at TIMESTAMP
);