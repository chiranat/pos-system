-- ==========================================
-- RESTAURANT POS DATABASE INITIALIZATION
-- Database: PostgreSQL
-- ==========================================

-- Enable UUID extension (จำเป็นสำหรับการใช้ UUID เป็น Primary Key)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ล้างข้อมูลเก่าก่อนสร้างใหม่ (สำหรับการ Dev)
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS dining_sessions CASCADE;
DROP TABLE IF EXISTS tables CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- ==========================================
-- 0. AUTHENTICATION & USERS (ผู้ใช้งานระบบ)
-- ==========================================

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE -- 'ADMIN', 'MANAGER', 'WAITER', 'KITCHEN'
);

-- Insert default roles
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('MANAGER');
INSERT INTO roles (name) VALUES ('WAITER');
INSERT INTO roles (name) VALUES ('KITCHEN');

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- เก็บ Password ที่ Hash แล้ว (เช่น BCrypt)
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role_id UUID NOT NULL REFERENCES roles(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- 1. CORE & LOCATION (จัดการพื้นที่และ Session)
-- ==========================================

CREATE TABLE tables (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    table_number VARCHAR(50) NOT NULL UNIQUE, -- เช่น 'T-01', 'T-02'
    capacity INTEGER NOT NULL DEFAULT 4,
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'OCCUPIED')),
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default tables
INSERT INTO tables (table_number, capacity) VALUES ('T-01', 4);
INSERT INTO tables (table_number, capacity) VALUES ('T-02', 4);
INSERT INTO tables (table_number, capacity) VALUES ('T-03', 2);
INSERT INTO tables (table_number, capacity) VALUES ('T-04', 6);
INSERT INTO tables (table_number, capacity) VALUES ('T-05', 4);
INSERT INTO tables (table_number, capacity) VALUES ('T-06', 8);

CREATE TABLE dining_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    table_id UUID NOT NULL REFERENCES tables(id),
    access_token VARCHAR(255) NOT NULL UNIQUE, -- Token สำหรับ Dynamic QR Code
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED')),
    total_amount DECIMAL(10, 2) DEFAULT 0.00, -- ยอดรวมสุทธิของ Session
    customer_count INTEGER DEFAULT 1
);

-- ==========================================
-- 2. MENU & CATALOG (เมนูอาหาร)
-- ==========================================

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    sort_order INTEGER DEFAULT 0
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_id UUID REFERENCES categories(id),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500), -- URL รูปภาพ
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- 3. ORDERING (การสั่งอาหาร)
-- ==========================================

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES dining_sessions(id), -- ผูกกับ Session ลูกค้าปัจจุบัน
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'COOKING', 'SERVED', 'CANCELLED')),
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price_snapshot DECIMAL(10, 2) NOT NULL, -- บันทึกราคา ณ เวลาที่สั่ง
    subtotal DECIMAL(10, 2) NOT NULL, -- (unit_price + options) * quantity
    notes TEXT -- โน้ตถึงครัว เช่น "ไม่ใส่ผักชี"
);

-- ==========================================
-- 4. PAYMENT (การเงิน)
-- ==========================================

CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES dining_sessions(id),
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- CASH, CREDIT_CARD, QR_PROMPT_PAY
    status VARCHAR(20) DEFAULT 'COMPLETED',
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

END $$;