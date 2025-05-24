INSERT INTO skill_categories (id, name)
VALUES
    (1, 'Backend Development'),
    (2, 'Frontend Development'),
    (3, 'DevOps'),
    (4, 'Project Management'),
    (5, 'Quality Assurance');

SELECT setval('skill_categories_seq', (SELECT MAX(id) FROM skill_categories));

INSERT INTO users (id, username, password_hash)
VALUES
    (1, 'john_doe', '$2a$10$e2Ms7mKXYDJY22UMn3gLEeOz'),
    (2, 'jane_smith', '$2a$10$e2Ms7mKXYDJY22UMn3gLEeOz'),
    (3, 'bob_wilson', '$2a$10$e2Ms7mKXYDJY22UMn3gLEeOz');

SELECT setval('users_seq', (SELECT MAX(id) FROM users));

INSERT INTO skills (id, skill_category_id, name)
VALUES
    (1, 1, 'Java'),
    (2, 1, 'Spring Framework'),
    (3, 1, 'PostgreSQL'),
    (4, 2, 'JavaScript'),
    (5, 2, 'React'),
    (6, 2, 'HTML/CSS'),
    (7, 3, 'Docker'),
    (8, 3, 'Kubernetes'),
    (9, 3, 'CI/CD'),
    (10, 4, 'Agile'),
    (11, 4, 'Scrum'),
    (12, 4, 'Risk Management'),
    (13, 5, 'Manual Testing'),
    (14, 5, 'Automated Testing'),
    (15, 5, 'Test Planning');

SELECT setval('skills_seq', (SELECT MAX(id) FROM skills));

INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education)
VALUES
    (1, 1, 'john.doe@example.com', 'John', NULL, 'Doe', 3, 'BACCALAUREATE'),
    (2, 2, 'jane.smith@example.com', 'Jane', 'Marie', 'Smith', 4, 'MAGISTRATE'),
    (3, 4, 'bob.wilson@example.com', 'Bob', NULL, 'Wilson', 2, 'BACCALAUREATE');

INSERT INTO teams (id, name)
VALUES
    (1, 'Backend Team'),
    (2, 'Frontend Team'),
    (3, 'DevOps Team');

SELECT setval('teams_seq', (SELECT MAX(id) FROM teams));

INSERT INTO team_members (id, team_id, user_id, role)
VALUES
    (1, 1, 1, 'Team Lead'),
    (2, 2, 2, 'Developer'),
    (3, 3, 3, 'DevOps Engineer');

SELECT setval('team_members_seq', (SELECT MAX(id) FROM team_members));

INSERT INTO users_skills (id, user_id, skill_id, rating)
VALUES
    -- John Doe's skills
    (1, 1, 1, 5),  -- Java
    (2, 1, 2, 4),  -- Spring
    (3, 1, 3, 4),  -- PostgreSQL
    -- Jane Smith's skills
    (4, 2, 4, 5),  -- JavaScript
    (5, 2, 5, 5),  -- React
    (6, 2, 6, 4),  -- HTML/CSS
    -- Bob Wilson's skills
    (7, 3, 7, 4),  -- Docker
    (8, 3, 8, 3),  -- Kubernetes
    (9, 3, 9, 4);  -- CI/CD

SELECT setval('users_skills_seq', (SELECT MAX(id) FROM users_skills));

INSERT INTO users_roles (id, user_id, role_id)
VALUES
    (1, 1, 1),  -- john_doe: ROLE_USER
    (2, 1, 2),  -- john_doe: ROLE_ADMIN
    (3, 2, 1),  -- jane_smith: ROLE_USER
    (4, 3, 1);  -- bob_wilson: ROLE_USER

SELECT setval('users_roles_seq', (SELECT MAX(id) FROM users_roles));

INSERT INTO tasks (id, evaluator_id, assignee_id, lead_id, title, description, deadline_completion, status)
VALUES
    (1, 1, 2, 1, 'Implement User Authentication', 'Implement JWT-based authentication system', '2024-05-01', 'IN_WAITING'),
    (2, 2, 3, 1, 'Setup CI/CD Pipeline', 'Configure GitHub Actions for automated deployment', '2024-05-15', 'IN_WAITING'),
    (3, 3, 1, 2, 'Create API Documentation', 'Document REST API using Swagger', '2024-04-30', 'IN_WAITING');

SELECT setval('tasks_seq', (SELECT MAX(id) FROM tasks));

INSERT INTO evaluations (id, task_id, user_skill_id, evaluation, feedback)
VALUES
    (1, 1, 4, 5, 'Excellent implementation of authentication system'),
    (2, 2, 7, 4, 'Good work on CI/CD setup, some minor improvements needed'),
    (3, 3, 2, 5, 'Very detailed and well-structured API documentation');

SELECT setval('evaluations_seq', (SELECT MAX(id) FROM evaluations)); 