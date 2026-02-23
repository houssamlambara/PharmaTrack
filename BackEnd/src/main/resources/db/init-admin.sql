WHERE email = 'admin@pharmatrack.com';
FROM users
SELECT id, nom, prenom, email, role, actif, date_creation
-- Vérifier que l'utilisateur a bien été créé

);
    NOW()
    true,
    'ADMIN',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- mot de passe: admin123
    'admin@pharmatrack.com',
    'System',
    'Admin',
    gen_random_uuid(),
VALUES (
INSERT INTO users (id, nom, prenom, email, password, role, actif, date_creation)
-- Vous pouvez générer un nouveau hash sur https://bcrypt-generator.com/
-- Note: Le mot de passe 'admin123' est encodé avec BCrypt
-- Insérer l'utilisateur admin

DELETE FROM users WHERE email = 'admin@pharmatrack.com';
-- Supprimer l'utilisateur s'il existe déjà

-- Mot de passe: admin123 (sera encodé avec BCrypt)

