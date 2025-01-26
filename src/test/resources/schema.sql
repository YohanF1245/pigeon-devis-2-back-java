-- Script de création des tables pour les tests (19/01/2024)

-- Suppression des tables existantes
DROP TABLE IF EXISTS verification_tokens;
DROP TABLE IF EXISTS businesses;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS role_type;

-- Création des tables
CREATE TYPE role_type AS ENUM ('USER', 'ADMIN');

CREATE TABLE users (
    user_id CHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    role role_type NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id)
);

CREATE TABLE verification_tokens (
    id CHAR(36) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id CHAR(36) NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE addresses (
    address_id CHAR(36) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    PRIMARY KEY (address_id)
);

CREATE TABLE businesses (
    business_id CHAR(36) NOT NULL,
    owner_id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    siret VARCHAR(14) NOT NULL UNIQUE,
    logo_path VARCHAR(255),
    address_id CHAR(36) NOT NULL,
    PRIMARY KEY (business_id),
    FOREIGN KEY (owner_id) REFERENCES users(user_id),
    FOREIGN KEY (address_id) REFERENCES addresses(address_id)
); 