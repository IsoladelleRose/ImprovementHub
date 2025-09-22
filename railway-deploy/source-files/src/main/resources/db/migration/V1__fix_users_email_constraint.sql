-- Fix users table email constraint
-- Make email column nullable to resolve constraint violation

ALTER TABLE users ALTER COLUMN email DROP NOT NULL;