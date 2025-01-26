-- Script de création des tables pour les tests (19/01/2024)

-- Suppression des tables existantes
DROP TABLE IF EXISTS businesses;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS verification_tokens;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS role_type;

-- Création du type ENUM pour les rôles
CREATE TYPE role_type AS ENUM ('ROLE_USER', 'ROLE_ADMIN');

-- Table des utilisateurs
CREATE TABLE users (
    user_id uuid PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role role_type NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    signature_path VARCHAR(255)
);

-- Table des tokens de vérification
CREATE TABLE verification_tokens (
    id uuid PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id uuid NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table des adresses
CREATE TABLE addresses (
    address_id uuid PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

-- Table des entreprises
CREATE TABLE businesses (
    business_id uuid PRIMARY KEY,
    owner_id uuid NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    siret VARCHAR(14) NOT NULL UNIQUE,
    logo_path VARCHAR(255),
    address_id uuid NOT NULL REFERENCES addresses(address_id) ON DELETE CASCADE
); 