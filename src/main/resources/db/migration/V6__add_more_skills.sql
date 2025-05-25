-- Добавляем новые категории навыков
INSERT INTO skill_categories (id, name)
VALUES
    (6, 'Mobile Development'),
    (7, 'Data Science'),
    (8, 'Cloud Technologies'),
    (9, 'Security'),
    (10, 'Architecture');

SELECT setval('skill_categories_seq', (SELECT MAX(id) FROM skill_categories));

-- Добавляем новые навыки по категориям
INSERT INTO skills (id, skill_category_id, name)
VALUES
    -- Backend Development (дополнительные)
    (16, 1, 'Python'),
    (17, 1, 'Node.js'),
    (18, 1, 'C#/.NET'),
    (19, 1, 'GraphQL'),
    (20, 1, 'MongoDB'),
    (21, 1, 'Redis'),
    (22, 1, 'RabbitMQ'),
    (23, 1, 'Kafka'),
    (24, 1, 'Microservices'),
    (25, 1, 'RESTful APIs'),

    -- Frontend Development (дополнительные)
    (26, 2, 'Vue.js'),
    (27, 2, 'Angular'),
    (28, 2, 'TypeScript'),
    (29, 2, 'Redux'),
    (30, 2, 'Webpack'),
    (31, 2, 'SASS/LESS'),
    (32, 2, 'Next.js'),
    (33, 2, 'Material UI'),
    (34, 2, 'Tailwind CSS'),
    (35, 2, 'Web Components'),

    -- DevOps (дополнительные)
    (36, 3, 'AWS'),
    (37, 3, 'Azure'),
    (38, 3, 'Google Cloud'),
    (39, 3, 'Ansible'),
    (40, 3, 'Jenkins'),
    (41, 3, 'GitLab CI'),
    (42, 3, 'Prometheus'),
    (43, 3, 'ELK Stack'),
    (44, 3, 'Terraform'),
    (45, 3, 'Helm'),

    -- Project Management (дополнительные)
    (46, 4, 'Kanban'),
    (47, 4, 'JIRA'),
    (48, 4, 'Confluence'),
    (49, 4, 'Stakeholder Management'),
    (50, 4, 'Budget Planning'),
    (51, 4, 'Resource Allocation'),
    (52, 4, 'Project Estimation'),
    (53, 4, 'Team Leadership'),
    (54, 4, 'Strategic Planning'),
    (55, 4, 'Change Management'),

    -- Quality Assurance (дополнительные)
    (56, 5, 'Selenium'),
    (57, 5, 'JUnit'),
    (58, 5, 'TestNG'),
    (59, 5, 'Cypress'),
    (60, 5, 'Performance Testing'),
    (61, 5, 'API Testing'),
    (62, 5, 'Load Testing'),
    (63, 5, 'Security Testing'),
    (64, 5, 'Test Automation'),
    (65, 5, 'Test Case Design'),

    -- Mobile Development (новая категория)
    (66, 6, 'Android/Kotlin'),
    (67, 6, 'iOS/Swift'),
    (68, 6, 'React Native'),
    (69, 6, 'Flutter'),
    (70, 6, 'Mobile UI Design'),
    (71, 6, 'Mobile Security'),
    (72, 6, 'App Performance'),
    (73, 6, 'Mobile Testing'),
    (74, 6, 'Push Notifications'),
    (75, 6, 'Mobile Analytics'),

    -- Data Science (новая категория)
    (76, 7, 'Machine Learning'),
    (77, 7, 'Deep Learning'),
    (78, 7, 'Data Analysis'),
    (79, 7, 'Python/Pandas'),
    (80, 7, 'R Programming'),
    (81, 7, 'TensorFlow'),
    (82, 7, 'Data Visualization'),
    (83, 7, 'Statistical Analysis'),
    (84, 7, 'NLP'),
    (85, 7, 'Big Data'),

    -- Cloud Technologies (новая категория)
    (86, 8, 'Cloud Architecture'),
    (87, 8, 'AWS Services'),
    (88, 8, 'Azure Services'),
    (89, 8, 'GCP Services'),
    (90, 8, 'Cloud Security'),
    (91, 8, 'Serverless'),
    (92, 8, 'Container Orchestration'),
    (93, 8, 'Cloud Storage'),
    (94, 8, 'Cloud Networking'),
    (95, 8, 'Cloud Cost Optimization'),

    -- Security (новая категория)
    (96, 9, 'Application Security'),
    (97, 9, 'Network Security'),
    (98, 9, 'Cryptography'),
    (99, 9, 'Security Auditing'),
    (100, 9, 'Penetration Testing'),
    (101, 9, 'Security Compliance'),
    (102, 9, 'Identity Management'),
    (103, 9, 'Threat Analysis'),
    (104, 9, 'Incident Response'),
    (105, 9, 'Security Tools'),

    -- Architecture (новая категория)
    (106, 10, 'System Design'),
    (107, 10, 'Enterprise Architecture'),
    (108, 10, 'Solution Architecture'),
    (109, 10, 'Domain-Driven Design'),
    (110, 10, 'Event-Driven Architecture'),
    (111, 10, 'Microservices Architecture'),
    (112, 10, 'Cloud-Native Architecture'),
    (113, 10, 'API Design'),
    (114, 10, 'Performance Engineering'),
    (115, 10, 'Scalability Patterns');

SELECT setval('skills_seq', (SELECT MAX(id) FROM skills));

-- Добавляем новые навыки существующим пользователям
INSERT INTO users_skills (id, user_id, skill_id, rating)
VALUES
    -- John Doe (Backend Lead)
    (31, 1, 16, 4),  -- Python
    (32, 1, 19, 5),  -- GraphQL
    (33, 1, 24, 5),  -- Microservices
    (34, 1, 106, 4), -- System Design
    (35, 1, 111, 5), -- Microservices Architecture

    -- Jane Smith (Frontend Lead)
    (36, 2, 26, 5),  -- Vue.js
    (37, 2, 28, 5),  -- TypeScript
    (38, 2, 32, 4),  -- Next.js
    (39, 2, 34, 5),  -- Tailwind CSS
    (40, 2, 70, 4),  -- Mobile UI Design

    -- Bob Wilson (DevOps Lead)
    (41, 3, 36, 5),  -- AWS
    (42, 3, 39, 4),  -- Ansible
    (43, 3, 42, 5),  -- Prometheus
    (44, 3, 44, 5),  -- Terraform
    (45, 3, 92, 4),  -- Container Orchestration

    -- Alice Johnson
    (46, 4, 17, 5),  -- Node.js
    (47, 4, 20, 4),  -- MongoDB
    (48, 4, 25, 5),  -- RESTful APIs
    (49, 4, 113, 4), -- API Design
    (50, 4, 114, 4), -- Performance Engineering

    -- Charlie Brown
    (51, 5, 37, 5),  -- Azure
    (52, 5, 40, 4),  -- Jenkins
    (53, 5, 43, 5),  -- ELK Stack
    (54, 5, 91, 4),  -- Serverless
    (55, 5, 95, 4),  -- Cloud Cost Optimization

    -- David Miller
    (56, 6, 27, 5),  -- Angular
    (57, 6, 29, 4),  -- Redux
    (58, 6, 31, 5),  -- SASS/LESS
    (59, 6, 33, 5),  -- Material UI
    (60, 6, 35, 4);  -- Web Components

SELECT setval('users_skills_seq', (SELECT MAX(id) FROM users_skills)); 