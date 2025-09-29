-- Fix partners table structure to match Partner entity
-- Add missing columns and remove obsolete ones

-- First, check if the old structure exists and fix it
-- Drop and recreate partners table with correct structure
DROP TABLE IF EXISTS partners CASCADE;

CREATE TABLE partners (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    vat_number VARCHAR(100),
    contact_person VARCHAR(255) NOT NULL,
    street_address VARCHAR(500),
    city VARCHAR(255),
    postal_code VARCHAR(20),
    country VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    interests_skills TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for email
CREATE INDEX idx_partners_email_v3 ON partners(email);