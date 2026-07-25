-- V1__init.sql
-- Baseline migration to set up essential PostgreSQL extensions

-- uuid-ossp provides functions to generate universally unique identifiers (UUIDs)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- pgcrypto provides cryptographic functions (useful for hashing if needed at DB level)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";