-- liquibase formatted sql

-- ===================================
-- ROLES
-- ===================================

-- changeset Thulasithsan:v1-create-roles-table
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

-- changeset Thulasithsan:v1-create-users-table
CREATE TABLE IF NOT EXISTS users (
    user_id              BIGSERIAL     PRIMARY KEY,
    first_name           VARCHAR(255)  NOT NULL,
    last_name            VARCHAR(255)  NOT NULL,
    email                VARCHAR(255)  NOT NULL UNIQUE,
    password             VARCHAR(255),
    previous_passwords   VARCHAR(1000),
    is_password_changed  BOOLEAN       DEFAULT FALSE,
    total_xp             INTEGER       DEFAULT 0,
    is_active            BOOLEAN       NOT NULL DEFAULT TRUE,

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

-- changeset Thulasithsan:v1-create-permissions-table
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

-- changeset Thulasithsan:v1-create-role_permissions-table
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

-- changeset Thulasithsan:v1-create-challenges-table
CREATE TABLE IF NOT EXISTS challenges (
    challenge_id         BIGSERIAL     PRIMARY KEY,
    title                VARCHAR(255)  NOT NULL,
    description          VARCHAR(255),
    type                 VARCHAR(50)   NOT NULL,
    level                INTEGER       NOT NULL,
    content              TEXT,
    time_limit_seconds   INTEGER       NOT NULL,
    required_accuracy    DOUBLE PRECISION NOT NULL,
    required_speed_wpm   DOUBLE PRECISION NOT NULL,
    win_xp               INTEGER       NOT NULL DEFAULT 0,
    lose_xp              INTEGER       NOT NULL DEFAULT 0,
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

-- changeset Thulasithsan:v1-create-user_challenges-table
CREATE TABLE IF NOT EXISTS user_challenges (
    user_challenge_id    BIGSERIAL     PRIMARY KEY,
    user_id              BIGINT        NOT NULL,
    challenge_id         BIGINT        NOT NULL,
    typed_content        TEXT,
    accuracy             DOUBLE PRECISION NOT NULL,
    speed                DOUBLE PRECISION NOT NULL,
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
-- BADGES (New Table)
-- ===================================

-- changeset Thulasithsan:v1-create-badge-table
CREATE TABLE IF NOT EXISTS badges (
    badge_id             BIGSERIAL       PRIMARY KEY,
    name                 VARCHAR(100)    NOT NULL,
    description          VARCHAR(255),

    criteria_type        VARCHAR(100)    NOT NULL,
    criteria_op          VARCHAR(10)     NOT NULL,
    criteria_value       DOUBLE PRECISION NOT NULL,
    is_active            BOOLEAN       NOT NULL DEFAULT TRUE,

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ     DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ     DEFAULT now()
);

-- Optional index for faster lookups by badge name
CREATE INDEX IF NOT EXISTS idx_badges_name ON badges(name);

-- rollback drop table badges;

-- ===================================
-- USER_BADGES (New Table)
-- ===================================

-- changeset Thulasithsan:v1-create-user-badges-table
CREATE TABLE IF NOT EXISTS user_badges (
    user_badge_id        BIGSERIAL     PRIMARY KEY,

    user_id              BIGINT        NOT NULL,
    badge_id             BIGINT        NOT NULL,
    challenge_id         BIGINT        NOT NULL,

    created_by           VARCHAR(255),
    created_date         TIMESTAMPTZ   DEFAULT now(),
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMPTZ   DEFAULT now(),

    CONSTRAINT fk_user_badge_user     FOREIGN KEY (user_id)      REFERENCES users(user_id)     ON DELETE CASCADE,
    CONSTRAINT fk_user_badge_badge    FOREIGN KEY (badge_id)     REFERENCES badges(badge_id)   ON DELETE CASCADE,
    CONSTRAINT fk_user_badge_challenge FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE
);
-- rollback drop table user_badges;

-- ===================================
-- DATA INSERTIONS
-- ===================================

-- Insert Roles
-- changeset Thulasithsan:v1-insert-initial-roles
INSERT INTO roles (name, description) VALUES
('SUPER_ADMIN', 'Full access to all system features'),
('ADMIN', 'Administrative access'),
('USER', 'Regular user access');
-- rollback DELETE FROM roles WHERE name IN ('SUPER_ADMIN', 'ADMIN', 'USER');

-- Insert Permissions
-- changeset Thulasithsan:v1-insert-initial-permissions
INSERT INTO permissions (name, description) VALUES
('Challenges', 'Challenge management'),
('User', 'User management');
-- rollback DELETE FROM permissions WHERE name IN ('Challenges', 'User');

-- Assign Permissions to SUPER_ADMIN with sub_permissions
-- changeset Thulasithsan:v1-assign-superadmin-subpermissions
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

-- changeset Thulasithsan:v1-insert-initial-challenges
INSERT INTO challenges (
  title, description, type, level,
  content, time_limit_seconds, required_speed_wpm, required_accuracy, win_xp,
  created_by, last_modified_by
) VALUES
('Level 1', 'Your first challenge', 'LEVEL', 1,
 'The quick brown fox jumps over the lazy dog. Typing practice helps you build muscle memory. Keep your fingers on the home row keys. Do not look at the keyboard. Accuracy is more important than speed at the beginning. Practice every day for better results.',
 90, 35, 90, 30, 1, 1),

('Level 2', 'Your second challenge', 'LEVEL', 2,
 'Typing is an essential skill in the modern world. It allows you to work faster and more efficiently. Learning to type without looking at the keyboard can save time and reduce mistakes. Focus on accuracy first, then improve your speed.',
 89, 36, 90, 32, 1, 1),

('Level 3', 'Your third challenge', 'LEVEL', 3,
 'A good typist uses all fingers and maintains a steady rhythm. Position your fingers on the home keys: A, S, D, F for the left hand and J, K, L, ; for the right. Keep your wrists up and your posture straight.',
 88, 37, 91, 34, 1, 1),

('Level 4', 'Your fourth challenge', 'LEVEL', 4,
 'Consistent typing practice can lead to big improvements over time. Set daily goals and track your progress. Avoid rushing. Take short breaks to relax your hands and eyes. Stay calm and patient with your learning process.',
 87, 38, 91, 36, 1, 1),

('Level 5', 'Your fifth challenge', 'LEVEL', 5,
 'When typing, always press the keys lightly and return your fingers to the home position. This helps you maintain speed and accuracy. Use all your fingers and keep your eyes on the screen, not the keyboard.',
 86, 39, 92, 38, 1, 1),

('Level 6', 'Your sixth challenge', 'LEVEL', 6,
 'Many professionals type every day without thinking about their technique. But good typing habits can save hours over the course of a week. Practicing proper technique will make typing feel effortless and natural.',
 85, 40, 92, 40, 1, 1),

('Level 7', 'Your seventh challenge', 'LEVEL', 7,
 'There are many online tools and games that help improve typing skills. Some even track your words per minute and accuracy rate. Set a timer and see how many words you can type correctly in one minute.',
 84, 41, 93, 42, 1, 1),

('Level 8', 'Your eighth challenge', 'LEVEL', 8,
 'As you type more, you will develop a sense of flow. This means your fingers will start to move without conscious effort. It is like learning to ride a bicycle – difficult at first, but easier with practice.',
 83, 42, 93, 44, 1, 1),

('Level 9', 'Your ninth challenge', 'LEVEL', 9,
 'As you type more, you will develop a sense of flow. This means your fingers will start to move without conscious effort. It is like learning to ride a bicycle – difficult at first, but easier with practice.',
 81, 43, 94, 46, 1, 1),

('Level 10', 'Your tenth challenge', 'LEVEL', 10,
 'Typing is not just about speed. It’s also about clarity and correctness. A fast typist who makes many errors is less effective than a slower, more accurate one. Focus on typing each word correctly before moving on.',
 80, 44, 94, 48, 1, 1);
-- rollback DELETE FROM challenges WHERE challenge_id BETWEEN 1 AND 10;

INSERT INTO badges (
  name, description, criteria_type, criteria_op, criteria_value, created_by, last_modified_by
) VALUES
('Accuracy Ace', 'Achieve 95%+ accuracy', 'accuracy', '>=', 95, 1, 1),
('Speedster', 'Reach 60+ WPM', 'wpm', '>=', 60, 1, 1),
('Consistent Pro', '10 consecutive challenges ≥ 90% accuracy', 'consecutive_accuracy', '>=', 90, 1, 1),
('Rising Star', 'Complete first 5 typing challenges', 'total_challenges', '>=', 5, 1, 1),
('Marathoner', 'Type for 60+ minutes cumulatively', 'total_time_minutes', '>=', 60, 1, 1),
('Challenge Champion', 'Win 5 typing competitions', 'wins', '>=', 5, 1, 1),

-- Note: Composite logic, you can handle this via application logic or extra column
('Speed & Accuracy', '50+ WPM with 95%+ accuracy', 'wpm_accuracy', '>=', 50, 1, 1),

('Daily Streak', 'Type every day for 7 consecutive days', 'daily_streak', '>=', 7, 1, 1),
('Night Owl', 'Complete challenge after 10 PM', 'hour', '>=', 22, 1, 1),
('Perfect Paragraph', '100% accuracy, zero errors on paragraph', 'accuracy', '=', 100, 1, 1),
('Comeback Kid', 'Win after losing 3 consecutive matches', 'comeback_wins', '>=', 1, 1, 1),
('Multi-Language Master', 'Complete challenges in 3 different languages', 'languages_completed', '>=', 3, 1, 1),

-- 'Top 1' is not a number, replace with 0 or handle via ranking logic
('Top Scorer', 'Highest XP in a competition', 'xp', '=', 0, 1, 1),

('Quick Finisher', 'Complete paragraph under 30 seconds', 'completion_time_s', '<=', 30, 1, 1),
('Error-Free Novice', '5 challenges with 100% accuracy', 'perfect_challenges', '>=', 5, 1, 1),
('Longest Streak', 'Maintain 30+ day daily streak', 'daily_streak', '>=', 30, 1, 1),
('Accuracy Veteran', 'Complete 100 challenges with ≥ 95% accuracy', 'total_challenges', '>=', 100, 1, 1),
('Speed Veteran', 'Complete 100 challenges with ≥ 50 WPM', 'total_challenges', '>=', 100, 1, 1),
('Typing Explorer', 'Complete challenges in 5 different categories', 'categories_completed', '>=', 5, 1, 1),
('Social Sharer', 'Share 10 badges on social media', 'badges_shared', '>=', 10, 1, 1);
