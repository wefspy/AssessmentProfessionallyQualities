-- sequence
CREATE SEQUENCE IF NOT EXISTS users_seq
    START WITH 1
    INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS roles_seq
    START WITH 1
    INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS users_roles_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS skill_categories_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS skills_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS users_skills_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS teams_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS team_members_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS tasks_seq
    START WITH 1
    INCREMENT BY 10;

CREATE SEQUENCE IF NOT EXISTS evaluations_seq
    START WITH 1
    INCREMENT BY 25;

-- pg_type
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'task_status') THEN
            CREATE TYPE task_status AS ENUM ('UNAVAILABLE', 'IN_WAITING', 'RATED', 'CANCELLED');
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'education') THEN
            CREATE TYPE education AS ENUM ('BACCALAUREATE', 'MAGISTRATE', 'POSTGRADUATE', 'SPECIALTY');
        END IF;
    END
$$;


-- tables
CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS skill_categories
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('skill_categories_seq'),
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_info
(
    id                     BIGINT PRIMARY KEY,
    main_skill_category_id BIGINT,
    first_name             VARCHAR(255) NOT NULL,
    middle_name            VARCHAR(255),
    last_name              VARCHAR(255) NOT NULL,
    course_number          SMALLINT,
    education              EDUCATION,
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (main_skill_category_id) REFERENCES skill_categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('roles_seq'),
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_roles
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('users_roles_seq'),
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skills
(
    id                BIGINT PRIMARY KEY DEFAULT nextval('skills_seq'),
    skill_category_id BIGINT       NOT NULL,
    name              VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (skill_category_id) REFERENCES skill_categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users_skills
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_skills_seq'),
    user_id  BIGINT   NOT NULL,
    skill_id BIGINT   NOT NULL,
    rating   SMALLINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teams
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('teams_seq'),
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS team_members
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('team_members_seq'),
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role    VARCHAR(255),
    FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks
(
    id                  BIGINT PRIMARY KEY DEFAULT nextval('tasks_seq'),
    evaluator_id        BIGINT       NOT NULL,
    assignee_id         BIGINT       NOT NULL,
    lead_id             BIGINT       NOT NULL,
    title               VARCHAR(255) NOT NULL,
    description         VARCHAR(255) NOT NULL,
    deadline_completion DATE         NOT NULL,
    status              TASK_STATUS  NOT NULL,
    FOREIGN KEY (evaluator_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (lead_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS evaluations
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('evaluations_seq'),
    task_id       BIGINT NOT NULL,
    user_skill_id BIGINT NOT NULL,
    evaluation    SMALLINT NOT NULL,
    feedback      TEXT,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (user_skill_id) REFERENCES users_skills (id) ON DELETE CASCADE
);
