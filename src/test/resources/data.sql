-- Script d'insertion des données de test (19/01/2024)
-- Les rôles sont maintenant gérés via un ENUM dans le schéma 

-- Insertion d'un utilisateur de test
INSERT INTO users (user_id, email, password, first_name, last_name, phone, role, enabled, verified)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'test@test.com', '$2a$10$rDkPvvAFV6GgAgzw0uXYQ.1EFYn8beB5HRZQSTHEeLKZrHzPHT2Hy', 'Test', 'User', '0123456789', 'ROLE_USER', true, true);

-- Insertion d'une adresse de test
INSERT INTO addresses (address_id, street, city, zip_code, country)
VALUES ('123e4567-e89b-12d3-a456-426614174001', '123 Test Street', 'Test City', '12345', 'Test Country');

-- Insertion d'une entreprise de test
INSERT INTO businesses (business_id, owner_id, name, siret, address_id)
VALUES ('123e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174000', 'Test Business', '12345678901234', '123e4567-e89b-12d3-a456-426614174001'); 