-- VibrantCovers MySQL Database Schema
-- Run this script to create the database and tables

-- Create database
CREATE DATABASE IF NOT EXISTS vibrantcovers;
USE vibrantcovers;

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS shipping_addresses;
DROP TABLE IF EXISTS billing_addresses;
DROP TABLE IF EXISTS configurations;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create configurations table
CREATE TABLE configurations (
    id VARCHAR(255) PRIMARY KEY,
    width INT NOT NULL,
    height INT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    color VARCHAR(50) NULL,
    model VARCHAR(50) NULL,
    material VARCHAR(50) NULL,
    finish VARCHAR(50) NULL,
    cropped_image_url VARCHAR(1000) NULL,
    INDEX idx_image_url (image_url(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create shipping_addresses table
CREATE TABLE shipping_addresses (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    state VARCHAR(100) NULL,
    phone_number VARCHAR(50) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create billing_addresses table
CREATE TABLE billing_addresses (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    state VARCHAR(100) NULL,
    phone_number VARCHAR(50) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create orders table
CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY,
    configuration_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL DEFAULT 'AWAITING_SHIPMENT',
    shipping_address_id VARCHAR(255) NULL,
    billing_address_id VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (configuration_id) REFERENCES configurations(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (shipping_address_id) REFERENCES shipping_addresses(id) ON DELETE SET NULL,
    FOREIGN KEY (billing_address_id) REFERENCES billing_addresses(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_configuration_id (configuration_id),
    INDEX idx_is_paid (is_paid),
    INDEX idx_created_at (created_at),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*
Enum value constraints (for reference, MySQL doesn't enforce these but JPA will)
Note: Prisma uses lowercase (awaiting_shipment), but Java uses UPPERCASE (AWAITING_SHIPMENT)
Since we're starting fresh, we use UPPERCASE to match Java enums
OrderStatus: FULFILLED, SHIPPED, AWAITING_SHIPMENT
PhoneModel: IPHONE11, IPHONE12, IPHONE13, IPHONE14, IPHONE15
CaseMaterial: SILICONE, POLYCARBONATE
CaseFinish: SMOOTH, TEXTURED
CaseColor: BLACK, STONE, ROSE, PINK, GREEN
*/

-- Verify tables were created
SHOW TABLES;

