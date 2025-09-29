-- Fix ideas table structure to match Idea entity
-- Replace old structure with correct columns

-- Drop and recreate ideas table with correct structure
DROP TABLE IF EXISTS ideas CASCADE;

CREATE TABLE ideas (
    id BIGSERIAL PRIMARY KEY,
    core_concept TEXT NOT NULL,
    problem_opportunity TEXT NOT NULL,
    wants_help BOOLEAN,
    user_role TEXT,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for email
CREATE INDEX idx_ideas_email_v4 ON ideas(email);