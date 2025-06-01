-- liquibase formatted sql

-- ===================================
-- ROLES
-- ===================================

-- changeset Thulasithsan:v4-create-roles-table
CREATE TABLE IF NOT EXISTS roles (
    role_id              BIGSERIAL     PRIMARY KEY,
    name                 VARCHAR(100)  NOT NULL UNIQUE,
    description          VARCHAR(255),

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ DEFAULT now()
);
-- rollback drop table roles;

-- ===================================
-- USERS
-- ===================================

-- changeset Thulasithsan:v4-create-users-table
CREATE TABLE IF NOT EXISTS users (
    user_id              BIGSERIAL     PRIMARY KEY,
    first_name           VARCHAR(255)  NOT NULL,
    last_name            VARCHAR(255)  NOT NULL,
    email                VARCHAR(255)  NOT NULL UNIQUE,
    password             VARCHAR(255)  NOT NULL,
    is_active            BOOLEAN       NOT NULL DEFAULT TRUE,
    is_password_changed  BOOLEAN       NOT NULL DEFAULT FALSE,
    previous_passwords   VARCHAR(1000),

    role_id              BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL,

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ DEFAULT now()
);
-- rollback drop table users;

-- ===================================
-- PERMISSIONS
-- ===================================

-- changeset Thulasithsan:v4-create-permissions-table
CREATE TABLE IF NOT EXISTS permissions (
    permission_id        BIGSERIAL     PRIMARY KEY,
    name                 VARCHAR(100)  NOT NULL UNIQUE,
    description          VARCHAR(255),

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ DEFAULT now()
);
-- rollback drop table permissions;

-- ===================================
-- ROLE_PERMISSIONS (Many-to-Many)
-- ===================================

-- changeset Thulasithsan:v4-create-role_permissions-table
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id              BIGINT NOT NULL,
    permission_id        BIGINT NOT NULL,
    sub_permissions      JSONB,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);
-- rollback drop table role_permissions;

-- ===================================
-- CHALLENGES
-- ===================================

-- changeset Thulasithsan:v4-create-challenges-table
CREATE TABLE IF NOT EXISTS challenges (
    challenge_id         BIGSERIAL     PRIMARY KEY,
    title                VARCHAR(255)  NOT NULL,
    description          VARCHAR(255),
    type                 VARCHAR(50)   NOT NULL,
    level                INTEGER       NOT NULL,
    content              TEXT,
    time_limit_seconds   INTEGER       NOT NULL,
    required_accuracy    INTEGER       NOT NULL,
    required_speed_wpm   INTEGER       NOT NULL,
    is_active            BOOLEAN       NOT NULL DEFAULT TRUE,

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ DEFAULT now()
);
-- rollback drop table challenges;

-- ===================================
-- USER_CHALLENGES (One-to-Many)
-- ===================================

-- changeset Thulasithsan:v4-create-user_challenges-table
CREATE TABLE IF NOT EXISTS user_challenges (
    user_challenge_id    BIGSERIAL     PRIMARY KEY,
    user_id              BIGINT        NOT NULL,
    challenge_id         BIGINT        NOT NULL,
    typed_content        TEXT,
    accuracy             INTEGER       NOT NULL,
    speed                INTEGER       NOT NULL,
    time_taken           INTEGER       NOT NULL,
    status               VARCHAR(50)   NOT NULL,

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ DEFAULT now(),

    CONSTRAINT fk_user_challenge_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_challenge_challenge FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE
);
-- rollback drop table user_challenges;

-- ===================================
-- DATA INSERTIONS
-- ===================================

-- Insert Roles
-- changeset Thulasithsan:v4-insert-initial-roles
INSERT INTO roles (name, description) VALUES
('SUPER_ADMIN', 'Full access to all system features'),
('ADMIN', 'Administrative access'),
('USER', 'Regular user access');
-- rollback DELETE FROM roles WHERE name IN ('SUPER_ADMIN', 'ADMIN', 'USER');

-- Insert Permissions
-- changeset Thulasithsan:v4-insert-initial-permissions
INSERT INTO permissions (name, description) VALUES
('Challenges', 'Challenge management'),
('User', 'User management');
-- rollback DELETE FROM permissions WHERE name IN ('Challenges', 'User');

-- Assign Permissions to SUPER_ADMIN with sub_permissions
-- changeset Thulasithsan:v5-assign-superadmin-subpermissions
INSERT INTO role_permissions (role_id, permission_id, sub_permissions)
SELECT r.role_id, p.permission_id, '["edit", "view", "delete", "create"]'::jsonb
FROM roles r, permissions p
WHERE r.name = 'SUPER_ADMIN' AND p.name IN ('Challenges', 'User');
-- rollback DELETE FROM role_permissions WHERE role_id = (SELECT role_id FROM roles WHERE name = 'SUPER_ADMIN');


-- Create Super Admin User
-- changeset Thulasithsan:v1-create-superadmin-user
INSERT INTO users (first_name, last_name, email, password, role_id, is_active, created_by, last_modified_by)
SELECT 'Super', 'Admin', 'superadmin@example.com',
       '$2a$12$B1WIOnzuD.4IYjUM1KH82.2WH.ct/4ekNSfpOk6PK4VHe/9DUU8m.',
       r.role_id, TRUE, 'system', 'system'
FROM roles r WHERE r.name = 'SUPER_ADMIN';
-- rollback DELETE FROM users WHERE email = 'superadmin@example.com';
