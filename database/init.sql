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

-- Insert default users
INSERT INTO users (username, password_hash, first_name, last_name, role_id, is_active)
SELECT
    'admin',
    '$2a$10$dYzKDPv2ojnaHBdoqcM5/.KwOdcIzACf1lfLTb0rcfOjvpqxDc/9C',
    'user',
    'admin',
    id,
    TRUE
FROM roles
WHERE name = 'ADMIN';

INSERT INTO users (username, password_hash, first_name, last_name, role_id, is_active)
SELECT
    'waiter',
    '$2a$10$oaDAqsbnUMEPikw5zoCO/O0PUhA7dMpB5bMn.ggkj9z.EM5U5Gq2q',
    'user',
    'waiter',
    id,
    TRUE
FROM roles
WHERE name = 'WAITER';

INSERT INTO users (username, password_hash, first_name, last_name, role_id, is_active)
SELECT
    'manager',
    '$2a$10$EjANEv0i9dwF86Gl.nCdRu/UpD7yGb0ihKpmqRcsjuaQXmlAVyiNy',
    'user',
    'manager',
    id,
    TRUE
FROM roles
WHERE name = 'MANAGER';

INSERT INTO users (username, password_hash, first_name, last_name, role_id, is_active)
SELECT
    'kitchen',
    '$2a$10$fahkpinFAKbQ3UTB3XKZyOQlcw0jlM1fEtSdxBMxNHXT5ejVz1KF.',
    'user',
    'kitchen',
    id,
    TRUE
FROM roles
WHERE name = 'KITCHEN';

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

-- Insert default categories
INSERT INTO categories (name, sort_order) VALUES
('Shabu Sets', 1),
('Meat & Pork', 2),
('Vegetables', 3),
('Drinks', 4);

-- Insert default products
-- Shabu Sets
INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Premium Beef Set', 'Wagyu beef, vegetables, and egg', 599.00, 'https://images.unsplash.com/photo-1555126634-323283e090fa?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Shabu Sets';

INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Pork Set', 'Kurobuta pork, vegetables, and egg', 399.00, 'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Shabu Sets';

-- Meat
INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Sliced Wagyu Beef', 'Premium A4 Wagyu', 299.00, 'https://images.unsplash.com/photo-1615937657715-bc7b4b7962c1?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Meat & Pork';

INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Sliced Pork Belly', 'Thinly sliced pork belly', 129.00, 'https://images.unsplash.com/photo-1615937691194-97dbd3f3dc29?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Meat & Pork';

-- Vegetables
INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Mixed Vegetable Set', 'Cabbage, Morning Glory, Corn, Mushroom', 89.00, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Vegetables';

-- Drinks
INSERT INTO products (category_id, name, description, price, image_url, is_available)
SELECT id, 'Coke', 'Can 325ml', 25.00, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=500&q=60', TRUE
FROM categories WHERE name = 'Drinks';

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