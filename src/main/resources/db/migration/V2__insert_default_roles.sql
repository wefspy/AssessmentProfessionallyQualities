INSERT INTO roles (id, name)
VALUES 
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

SELECT setval('roles_seq', (SELECT MAX(id) FROM roles));
