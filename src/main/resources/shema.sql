-- ============================================
-- SISTEMA DE CONTROL DE ACCESOS - COWORKING
-- Script de creación de base de datos
-- ============================================

-- Crear la base de datos (ejecutar por separado si es necesario)
-- CREATE DATABASE coworking_db;

-- ============================================
-- TABLA: users
-- Almacena la información de cada miembro
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    phone       VARCHAR(20),
    membership  VARCHAR(50) DEFAULT 'BASIC',
                -- Valores posibles: BASIC, PREMIUM, ENTERPRISE
    active      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLA: access_records
-- Registra cada entrada y salida al espacio
-- ============================================
CREATE TABLE IF NOT EXISTS access_records (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    access_type VARCHAR(10) NOT NULL,
                -- Valores: 'ENTRY' o 'EXIT'
    entry_time  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exit_time   TIMESTAMP,          -- NULL mientras el usuario está dentro
    notes       TEXT,               -- Observaciones opcionales
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Llave foránea: cada registro pertenece a un usuario
    CONSTRAINT fk_access_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE   -- Si se borra el usuario, se borran sus registros
);

-- ============================================
-- ÍNDICES: mejoran la velocidad de búsquedas
-- ============================================
CREATE INDEX idx_access_user_id ON access_records(user_id);
CREATE INDEX idx_access_entry_time ON access_records(entry_time);
CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- DATOS DE PRUEBA (seed data)
-- ============================================
INSERT INTO users (full_name, email, phone, membership) VALUES
    ('Ana García',    'ana.garcia@email.com',    '+52-555-0001', 'PREMIUM'),
    ('Carlos López',  'carlos.lopez@email.com',  '+52-555-0002', 'BASIC'),
    ('María Torres',  'maria.torres@email.com',  '+52-555-0003', 'ENTERPRISE'),
    ('Juan Martínez', 'juan.martinez@email.com', '+52-555-0004', 'BASIC');

INSERT INTO access_records (user_id, access_type, entry_time, exit_time) VALUES
    (1, 'ENTRY', '2024-01-15 08:00:00', '2024-01-15 17:30:00'),
    (1, 'ENTRY', '2024-01-16 09:15:00', NULL),
    (2, 'ENTRY', '2024-01-15 10:00:00', '2024-01-15 14:00:00'),
    (3, 'ENTRY', '2024-01-16 08:30:00', '2024-01-16 20:00:00');