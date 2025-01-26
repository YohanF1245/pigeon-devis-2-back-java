-- Script d'insertion des données de test
-- Auteur: Yohan
-- Date: 19/01/2024

-- Insertion des utilisateurs de test
INSERT INTO users (user_id, email, password, first_name, last_name, phone, enabled, verified, role)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'admin@test.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'Admin', 'Test', '0123456789', true, true, 'ROLE_ADMIN'),
    ('22222222-2222-2222-2222-222222222222', 'user@test.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'User', 'Test', '9876543210', true, true, 'ROLE_USER');

-- Insertion des adresses de test
INSERT INTO addresses (address_id, street_number, street_name, zip_code, city)
VALUES 
    ('33333333-3333-3333-3333-333333333333', '1', 'Rue de Test', '75000', 'Paris'),
    ('44444444-4444-4444-4444-444444444444', '2', 'Avenue de Test', '69000', 'Lyon');

-- Insertion des entreprises de test
INSERT INTO businesses (business_id, owner_id, siret, ape_code, tax_code, address_id)
VALUES 
    ('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', '12345678901234', '6201Z', 'FR12345678901', '33333333-3333-3333-3333-333333333333'),
    ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222', '98765432109876', '6202A', 'FR98765432109', '44444444-4444-4444-4444-444444444444'); 