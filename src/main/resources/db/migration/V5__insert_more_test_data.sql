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
    -- Задачи для Backend команды
    (4, 1, 2, 1, 'Implement User Profile', 'Create user profile page with editing capabilities', '2024-04-15', 'IN_WAITING'),
    (5, 1, 2, 1, 'Add Password Reset', 'Implement password reset functionality with email confirmation', '2024-04-20', 'IN_WAITING'),
    (6, 1, 2, 1, 'Optimize Database Queries', 'Review and optimize slow database queries', '2024-04-25', 'IN_WAITING'),
    (7, 1, 3, 1, 'Setup Caching Layer', 'Implement Redis caching for frequently accessed data', '2024-05-01', 'IN_WAITING'),
    (8, 1, 3, 1, 'API Rate Limiting', 'Implement rate limiting for public API endpoints', '2024-05-05', 'IN_WAITING'),
    (9, 1, 2, 1, 'Database Backup System', 'Setup automated database backup system', '2024-05-10', 'IN_WAITING'),
    (10, 1, 3, 1, 'Logging System', 'Implement centralized logging system with ELK stack', '2024-05-15', 'IN_WAITING'),

    -- Задачи для Frontend команды
    (11, 2, 1, 2, 'Design Dashboard UI', 'Create responsive dashboard interface', '2024-04-20', 'IN_WAITING'),
    (12, 2, 3, 2, 'Implement Dark Theme', 'Add dark theme support across all pages', '2024-04-25', 'IN_WAITING'),
    (13, 2, 1, 2, 'Mobile Responsiveness', 'Ensure all pages are mobile-friendly', '2024-04-30', 'IN_WAITING'),
    (14, 2, 3, 2, 'Performance Optimization', 'Optimize frontend performance and loading times', '2024-05-05', 'IN_WAITING'),
    (15, 2, 1, 2, 'Add Data Visualization', 'Implement charts and graphs for analytics', '2024-05-10', 'IN_WAITING'),
    (16, 2, 3, 2, 'Accessibility Improvements', 'Enhance website accessibility (WCAG compliance)', '2024-05-15', 'IN_WAITING'),
    (17, 2, 1, 2, 'Unit Tests Coverage', 'Increase unit tests coverage for frontend code', '2024-05-20', 'IN_WAITING'),

    -- Задачи для DevOps команды
    (18, 3, 2, 3, 'Setup Monitoring System', 'Implement Prometheus and Grafana monitoring', '2024-04-25', 'IN_WAITING'),
    (19, 3, 1, 3, 'Container Orchestration', 'Setup Kubernetes cluster for production', '2024-04-30', 'IN_WAITING'),
    (20, 3, 2, 3, 'CI/CD Pipeline', 'Improve CI/CD pipeline with automated testing', '2024-05-05', 'IN_WAITING'),
    (21, 3, 1, 3, 'Security Scanning', 'Implement automated security scanning in pipeline', '2024-05-10', 'IN_WAITING'),
    (22, 3, 2, 3, 'Infrastructure as Code', 'Convert infrastructure setup to Terraform', '2024-05-15', 'IN_WAITING'),
    (23, 3, 1, 3, 'Backup Strategy', 'Implement disaster recovery and backup strategy', '2024-05-20', 'IN_WAITING'),
    (24, 3, 2, 3, 'Load Testing', 'Setup automated load testing with K6', '2024-05-25', 'IN_WAITING'),

    -- Кросс-функциональные задачи
    (25, 1, 2, 3, 'API Documentation', 'Update API documentation with new endpoints', '2024-05-01', 'IN_WAITING'),
    (26, 2, 3, 1, 'Security Audit', 'Conduct security audit of the application', '2024-05-05', 'IN_WAITING'),
    (27, 3, 1, 2, 'Performance Testing', 'Conduct load and stress testing', '2024-05-10', 'IN_WAITING'),
    (28, 1, 3, 2, 'Code Review Process', 'Improve code review guidelines and process', '2024-05-15', 'IN_WAITING'),
    (29, 2, 1, 3, 'Technical Debt', 'Address critical technical debt items', '2024-05-20', 'IN_WAITING'),
    (30, 3, 2, 1, 'Dependency Updates', 'Update all dependencies to latest versions', '2024-05-25', 'IN_WAITING'),

    -- Задачи по улучшению качества
    (31, 1, 2, 3, 'Code Quality Gates', 'Setup SonarQube quality gates', '2024-05-05', 'IN_WAITING'),
    (32, 2, 3, 1, 'E2E Testing', 'Implement end-to-end testing with Cypress', '2024-05-10', 'IN_WAITING'),
    (33, 3, 1, 2, 'Performance Metrics', 'Setup performance monitoring and alerts', '2024-05-15', 'IN_WAITING'),
    (34, 1, 3, 2, 'Documentation Update', 'Update technical documentation', '2024-05-20', 'IN_WAITING'),
    (35, 2, 1, 3, 'Accessibility Testing', 'Implement automated accessibility testing', '2024-05-25', 'IN_WAITING'),
    (36, 3, 2, 1, 'Security Compliance', 'Ensure GDPR and security compliance', '2024-05-30', 'IN_WAITING'),

    -- Задачи по оптимизации
    (37, 1, 2, 3, 'Database Indexing', 'Optimize database indexes for performance', '2024-05-10', 'IN_WAITING'),
    (38, 2, 3, 1, 'Frontend Bundling', 'Optimize frontend bundle size', '2024-05-15', 'IN_WAITING'),
    (39, 3, 1, 2, 'Cache Strategy', 'Implement efficient caching strategy', '2024-05-20', 'IN_WAITING'),
    (40, 1, 3, 2, 'API Optimization', 'Optimize API response times', '2024-05-25', 'IN_WAITING'),
    (41, 2, 1, 3, 'Image Optimization', 'Implement image optimization pipeline', '2024-05-30', 'IN_WAITING'),
    (42, 3, 2, 1, 'Resource Usage', 'Optimize server resource usage', '2024-06-05', 'IN_WAITING'),

    -- Задачи по безопасности
    (43, 1, 2, 3, 'Security Headers', 'Implement security headers', '2024-05-15', 'IN_WAITING'),
    (44, 2, 3, 1, 'XSS Prevention', 'Implement XSS prevention measures', '2024-05-20', 'IN_WAITING'),
    (45, 3, 1, 2, 'CSRF Protection', 'Enhance CSRF protection', '2024-05-25', 'IN_WAITING'),
    (46, 1, 3, 2, 'SQL Injection', 'Audit and prevent SQL injection', '2024-05-30', 'IN_WAITING'),
    (47, 2, 1, 3, 'Authentication', 'Enhance authentication security', '2024-06-05', 'IN_WAITING'),
    (48, 3, 2, 1, 'Vulnerability Scan', 'Regular vulnerability scanning', '2024-06-10', 'IN_WAITING');

SELECT setval('tasks_seq', (SELECT MAX(id) FROM tasks));

INSERT INTO evaluations (id, task_id, user_skill_id, evaluation, feedback)
VALUES
    (4, 4, 1, 5, 'Excellent implementation of user profile functionality'),
    (5, 5, 2, 4, 'Good work on password reset feature'),
    (6, 6, 3, 5, 'Great optimization of database queries'),
    (7, 11, 4, 4, 'Well-designed dashboard interface'),
    (8, 12, 5, 5, 'Perfect implementation of dark theme'),
    (9, 13, 6, 4, 'Good mobile responsiveness implementation'),
    (10, 18, 7, 5, 'Excellent setup of monitoring system'),
    (11, 19, 8, 4, 'Good work on Kubernetes setup'),
    (12, 20, 9, 5, 'Great improvements to CI/CD pipeline');

SELECT setval('evaluations_seq', (SELECT MAX(id) FROM evaluations)); 