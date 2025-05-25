INSERT INTO users (id, username, password_hash)
VALUES
    (4, 'alice_johnson', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (5, 'charlie_brown', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (6, 'david_miller', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (7, 'emma_davis', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (8, 'frank_white', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (9, 'grace_lee', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.'),
    (10, 'henry_garcia', '$2a$10$f831X/mZRGsls3MiVPvtnOh6kgb6.WQNWpg5UEvLHyfh8V83Wpns.');

SELECT setval('users_seq', (SELECT MAX(id) FROM users));

INSERT INTO users_info (id, main_skill_category_id, email, first_name, middle_name, last_name, course_number, education)
VALUES
    (4, 1, 'alice.johnson@example.com', 'Alice', 'Rose', 'Johnson', 3, 'BACCALAUREATE'),
    (5, 3, 'charlie.brown@example.com', 'Charlie', NULL, 'Brown', 4, 'MAGISTRATE'),
    (6, 2, 'david.miller@example.com', 'David', 'James', 'Miller', 2, 'BACCALAUREATE'),
    (7, 5, 'emma.davis@example.com', 'Emma', 'Grace', 'Davis', 3, 'MAGISTRATE'),
    (8, 1, 'frank.white@example.com', 'Frank', NULL, 'White', 4, 'POSTGRADUATE'),
    (9, 2, 'grace.lee@example.com', 'Grace', 'Min', 'Lee', 2, 'BACCALAUREATE'),
    (10, 4, 'henry.garcia@example.com', 'Henry', 'Luis', 'Garcia', 3, 'MAGISTRATE');

INSERT INTO teams (id, name)
VALUES
    (4, 'Mobile Team'),
    (5, 'QA Team'),
    (6, 'UI/UX Team'),
    (7, 'Security Team');

SELECT setval('teams_seq', (SELECT MAX(id) FROM teams));

INSERT INTO team_members (id, team_id, user_id, role)
VALUES
    (4, 1, 4, 'Senior Developer'),
    (5, 2, 5, 'Frontend Lead'),
    (6, 3, 6, 'DevOps Engineer'),
    (7, 4, 7, 'Mobile Developer'),
    (8, 5, 8, 'QA Lead'),
    (9, 6, 9, 'UI Designer'),
    (10, 7, 10, 'Security Engineer'),
    (11, 1, 2, 'Backend Developer'),
    (12, 2, 3, 'Frontend Developer'),
    (13, 3, 4, 'System Administrator'),
    (14, 4, 5, 'Mobile Developer'),
    (15, 5, 6, 'QA Engineer');

SELECT setval('team_members_seq', (SELECT MAX(id) FROM team_members));

INSERT INTO users_roles (id, user_id, role_id)
VALUES
    (5, 4, 1),  -- ROLE_USER
    (6, 5, 1),
    (7, 6, 1),
    (8, 7, 1),
    (9, 8, 1),
    (10, 9, 1),
    (11, 10, 1),
    (12, 4, 2); -- ROLE_ADMIN for Alice

SELECT setval('users_roles_seq', (SELECT MAX(id) FROM users_roles));

INSERT INTO users_skills (id, user_id, skill_id, rating)
VALUES
    -- Alice Johnson's skills
    (10, 4, 1, 5),  -- Java
    (11, 4, 2, 5),  -- Spring
    (12, 4, 3, 4),  -- PostgreSQL
    -- Charlie Brown's skills
    (13, 5, 7, 5),  -- Docker
    (14, 5, 8, 4),  -- Kubernetes
    (15, 5, 9, 5),  -- CI/CD
    -- David Miller's skills
    (16, 6, 4, 4),  -- JavaScript
    (17, 6, 5, 5),  -- React
    (18, 6, 6, 5),  -- HTML/CSS
    -- Emma Davis's skills
    (19, 7, 13, 5), -- Manual Testing
    (20, 7, 14, 4), -- Automated Testing
    (21, 7, 15, 5), -- Test Planning
    -- Frank White's skills
    (22, 8, 1, 5),  -- Java
    (23, 8, 2, 5),  -- Spring
    (24, 8, 7, 4),  -- Docker
    -- Grace Lee's skills
    (25, 9, 4, 5),  -- JavaScript
    (26, 9, 5, 5),  -- React
    (27, 9, 6, 5),  -- HTML/CSS
    -- Henry Garcia's skills
    (28, 10, 10, 5), -- Agile
    (29, 10, 11, 5), -- Scrum
    (30, 10, 12, 4); -- Risk Management

SELECT setval('users_skills_seq', (SELECT MAX(id) FROM users_skills));

INSERT INTO tasks (id, evaluator_member_id, assignee_member_id, lead_member_id, title, description, deadline_completion, status)
VALUES
    (4, 4, 11, 4, 'Implement User Profile', 'Create user profile page with editing capabilities', '2024-04-15', 'IN_WAITING'),
    (5, 5, 12, 5, 'Design Dashboard UI', 'Create responsive dashboard interface', '2024-04-20', 'IN_WAITING'),
    (6, 6, 13, 6, 'Setup Monitoring System', 'Implement Prometheus and Grafana monitoring', '2024-04-25', 'IN_WAITING'),
    (7, 8, 14, 8, 'Mobile App Testing', 'Perform comprehensive testing of mobile application', '2024-05-01', 'IN_WAITING'),
    (8, 4, 15, 4, 'API Integration Tests', 'Write integration tests for REST API', '2024-05-05', 'IN_WAITING'),
    (9, 5, 11, 5, 'Implement Search Feature', 'Add search functionality to the platform', '2024-05-10', 'IN_WAITING'),
    (10, 6, 12, 6, 'Database Optimization', 'Optimize database queries and indexes', '2024-05-15', 'IN_WAITING'),
    (11, 8, 13, 8, 'Security Audit', 'Perform security audit of the application', '2024-05-20', 'IN_WAITING'),
    (12, 4, 14, 4, 'Push Notification System', 'Implement push notifications for mobile app', '2024-05-25', 'IN_WAITING'),
    (13, 5, 15, 5, 'Performance Testing', 'Conduct load and stress testing', '2024-06-01', 'IN_WAITING');

SELECT setval('tasks_seq', (SELECT MAX(id) FROM tasks));

INSERT INTO evaluations (id, task_id, user_skill_id, evaluation, feedback)
VALUES
    (4, 4, 10, 5, 'Excellent implementation of user profile functionality'),
    (5, 5, 17, 4, 'Good design work, some minor improvements in responsiveness needed'),
    (6, 6, 13, 5, 'Great setup of monitoring system with detailed documentation'),
    (7, 7, 20, 4, 'Thorough testing performed, good bug reports'),
    (8, 8, 11, 5, 'Comprehensive test coverage of API endpoints'),
    (9, 9, 16, 4, 'Search feature works well, could use some optimization'),
    (10, 10, 12, 5, 'Significant improvement in query performance'),
    (11, 11, 14, 4, 'Detailed security report with actionable recommendations'),
    (12, 12, 18, 5, 'Push notification system works flawlessly'),
    (13, 13, 21, 4, 'Good performance test results with detailed analysis');

SELECT setval('evaluations_seq', (SELECT MAX(id) FROM evaluations)); 