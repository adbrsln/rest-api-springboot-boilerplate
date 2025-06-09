-- =====================================================================================
--                        INITIAL DATA FOR ROBUST API DEMO
-- =====================================================================================
-- This script is executed on startup by Spring Boot when using an embedded database (H2).
--
-- IMPORTANT: The order of insertion matters due to foreign key constraints.
-- Users must be inserted BEFORE the Todos that reference them.
-- =====================================================================================


-- -------------------------------------------------------------------------------------
--                                    INSERT USERS
-- -------------------------------------------------------------------------------------
-- Note: In a real-world application, passwords should NEVER be stored in plain text.
-- They must be hashed using a strong algorithm like BCrypt.
-- -------------------------------------------------------------------------------------

INSERT INTO users (id, username, email, password) VALUES
(1, 'alice', 'alice@example.com', 'password123'),  -- User with ID 1
(2, 'bob', 'bob@example.com', 'password456');    -- User with ID 2


-- -------------------------------------------------------------------------------------
--                                     INSERT TODOS
-- -------------------------------------------------------------------------------------
-- Each todo must be linked to an existing user via the 'user_id' foreign key.
-- We will assign some todos to Alice (user_id=1) and some to Bob (user_id=2).
-- The 'created_at' and 'updated_at' columns will be handled by the @PrePersist
-- and @PreUpdate annotations in the entity, but we can set them here for clarity.
-- -------------------------------------------------------------------------------------

INSERT INTO todos (id, title, description, completed, user_id, created_at, updated_at) VALUES
-- Todos for Alice (user_id=1)
(1, 'Buy groceries', 'Milk, Bread, Cheese, and Eggs', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Finish REST API project', 'Implement the User and Todo endpoints with proper error handling.', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Call the bank', 'Inquire about the new credit card offer.', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Todo for Bob (user_id=2)
(4, 'Plan weekend trip', NULL, false, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- =====================================================================================
--       MANUALLY UPDATE THE SEQUENCE GENERATORS (OPTIONAL BUT GOOD PRACTICE)
-- =====================================================================================
-- When you manually insert data with primary keys, the database's auto-increment
-- sequence is not updated. The next time you try to INSERT via your application,
-- it might try to use an ID that's already taken (e.g., ID 1), causing a "Unique
-- constraint violation" error.
--
-- The commands below tell the sequences to start counting from the next available ID.
-- This syntax is for H2. It varies for other databases (e.g., PostgreSQL uses setval).
-- =====================================================================================

ALTER TABLE users ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM users) + 1;
ALTER TABLE todos ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM todos) + 1;