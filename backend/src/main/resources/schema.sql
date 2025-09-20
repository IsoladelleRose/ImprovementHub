-- ImprovementHub Database Schema
-- This file will be automatically executed by Spring Boot if needed

-- Create partners table
CREATE TABLE IF NOT EXISTS partners (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    vat_number VARCHAR(50),
    contact_person VARCHAR(255) NOT NULL,
    street_address VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    email VARCHAR(255) UNIQUE NOT NULL,
    interests_skills TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ideas table
CREATE TABLE IF NOT EXISTS ideas (
    id BIGSERIAL PRIMARY KEY,
    core_concept TEXT NOT NULL,
    problem_opportunity TEXT NOT NULL,
    wants_help BOOLEAN,
    user_role TEXT,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_partners_email ON partners(email);
CREATE INDEX IF NOT EXISTS idx_partners_company_name ON partners(company_name);
CREATE INDEX IF NOT EXISTS idx_ideas_email ON ideas(email);
CREATE INDEX IF NOT EXISTS idx_ideas_wants_help ON ideas(wants_help);
CREATE INDEX IF NOT EXISTS idx_ideas_created_at ON ideas(created_at);
CREATE INDEX IF NOT EXISTS idx_partners_created_at ON partners(created_at);