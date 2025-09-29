-- Migration to rename email_address column to email in users table
-- This aligns the database schema with the User entity

ALTER TABLE users RENAME COLUMN email_address TO email;