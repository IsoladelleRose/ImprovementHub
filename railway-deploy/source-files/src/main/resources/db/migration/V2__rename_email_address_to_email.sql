-- Migration to rename email_address column to email in users table
-- This aligns the database schema with the User entity

-- Only rename if email_address column exists and email doesn't
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users'
        AND column_name = 'email_address'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users'
        AND column_name = 'email'
    ) THEN
        ALTER TABLE users RENAME COLUMN email_address TO email;
    END IF;
END $$;