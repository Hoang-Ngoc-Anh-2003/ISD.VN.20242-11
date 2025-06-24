-- =====================================================
-- AIMS COMPLETE DATABASE SCHEMA FOR POSTGRESQL (2 roles: manager, customer)
-- =====================================================

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS rush_orders CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS delivery_info CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orderdetails CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS cartitems CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS cds CASCADE;
DROP TABLE IF EXISTS lps CASCADE;
DROP TABLE IF EXISTS dvds CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- =====================================================
-- USERS AND ROLES
-- =====================================================

-- Roles table
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT roles_role_name_check CHECK (role_name IN ('manager', 'customer'))
);

-- Users table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    blocked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User roles junction table (for multiple roles per user)
CREATE TABLE user_roles (
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    role_id INTEGER REFERENCES roles(role_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id)
);

-- =====================================================
-- PRODUCTS
-- =====================================================

-- Main products table
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('book', 'CD', 'LP', 'DVD')),
    value NUMERIC(10,2) NOT NULL, -- Import price
    price NUMERIC(10,2) NOT NULL, -- Selling price
    barcode VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    weight VARCHAR(100),
    dimensions VARCHAR(255),
    warehouse_entry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE books (
    book_id INTEGER PRIMARY KEY REFERENCES products(product_id) ON DELETE CASCADE,
    authors TEXT NOT NULL,
    cover_type VARCHAR(20) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    num_pages INTEGER,
    language VARCHAR(50),
    genre VARCHAR(100),
    CONSTRAINT books_cover_type_check CHECK (cover_type IN ('paperback', 'hardcover'))
);

-- CDs table
CREATE TABLE cds (
    cd_id INTEGER PRIMARY KEY REFERENCES products(product_id) ON DELETE CASCADE,
    artists TEXT NOT NULL,
    record_label VARCHAR(255) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100) NOT NULL,
    release_date DATE
);

-- LPs table
CREATE TABLE lps (
    lp_id INTEGER PRIMARY KEY REFERENCES products(product_id) ON DELETE CASCADE,
    artists TEXT NOT NULL,
    record_label VARCHAR(255) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100) NOT NULL,
    release_date DATE
);

-- DVDs table
CREATE TABLE dvds (
    dvd_id INTEGER PRIMARY KEY REFERENCES products(product_id) ON DELETE CASCADE,
    disc_type VARCHAR(20) NOT NULL,
    director VARCHAR(255) NOT NULL,
    runtime INTEGER NOT NULL,
    studio VARCHAR(255) NOT NULL,
    language VARCHAR(50) NOT NULL,
    subtitles VARCHAR(255) NOT NULL,
    release_date DATE,
    genre VARCHAR(100),
    CONSTRAINT dvds_disc_type_check CHECK (disc_type IN ('Blu-ray', 'HD-DVD'))
);

-- =====================================================
-- SHOPPING CART
-- =====================================================

-- Carts table
CREATE TABLE carts (
    cart_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cart items table
CREATE TABLE cartitems (
    cart_id INTEGER REFERENCES carts(cart_id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(product_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id, product_id),
    CONSTRAINT cartitems_quantity_check CHECK (quantity > 0)
);

-- =====================================================
-- ORDERS
-- =====================================================

-- Orders table
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    is_rush_order BOOLEAN DEFAULT FALSE,
    total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,
    vat NUMERIC(10,2) DEFAULT 0,
    delivery_fee NUMERIC(10,2) DEFAULT 0,
    total_payment NUMERIC(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_status_check CHECK (status IN ('pending', 'approved', 'rejected', 'cancelled', 'shipped', 'delivered'))
);

-- Order details table
CREATE TABLE orderdetails (
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(product_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT orderdetails_quantity_check CHECK (quantity > 0)
);

-- =====================================================
-- DELIVERY AND PAYMENT
-- =====================================================

-- Delivery info table
CREATE TABLE delivery_info (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    receiver_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    province VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    shipping_method VARCHAR(50) NOT NULL,
    shipping_fee NUMERIC(10,2) NOT NULL DEFAULT 0,
    status VARCHAR(50) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT delivery_info_status_check CHECK (status IN ('pending', 'shipping', 'delivered', 'cancelled'))
);

-- Payments table
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    amount NUMERIC(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT payments_status_check CHECK (status IN ('pending', 'completed', 'failed', 'refunded'))
);

-- Rush orders table
CREATE TABLE rush_orders (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    customer_name VARCHAR(100) NOT NULL,
    delivery_time TIMESTAMP NOT NULL,
    rush_fee NUMERIC(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Users indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Products indexes
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_barcode ON products(barcode);
CREATE INDEX idx_products_title ON products(title);

-- Orders indexes
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date);

-- Cart indexes
CREATE INDEX idx_cartitems_cart_id ON cartitems(cart_id);
CREATE INDEX idx_cartitems_product_id ON cartitems(product_id);

-- =====================================================
-- INSERT DEFAULT DATA
-- =====================================================

-- Insert default roles
INSERT INTO roles (role_name, description) VALUES
('manager', 'Product manager with management access'),
('customer', 'Regular customer user');

-- Insert default manager user (password: admin123)
INSERT INTO users (username, password, email, full_name, phone, blocked) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@aims.com', 'Product Manager', '0123456789', false);

-- Assign manager role to admin user
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- =====================================================
-- TRIGGERS FOR UPDATED_AT
-- =====================================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_carts_updated_at BEFORE UPDATE ON carts FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- COMMENTS
-- =====================================================

COMMENT ON TABLE users IS 'User accounts for the AIMS system';
COMMENT ON TABLE products IS 'Main products table containing all product types';
COMMENT ON TABLE books IS 'Book-specific product details';
COMMENT ON TABLE cds IS 'CD-specific product details';
COMMENT ON TABLE lps IS 'LP-specific product details';
COMMENT ON TABLE dvds IS 'DVD-specific product details';
COMMENT ON TABLE orders IS 'Customer orders';
COMMENT ON TABLE orderdetails IS 'Order line items';
COMMENT ON TABLE carts IS 'Shopping carts';
COMMENT ON TABLE cartitems IS 'Shopping cart items';
COMMENT ON TABLE delivery_info IS 'Delivery information for orders';
COMMENT ON TABLE payments IS 'Payment information for orders';
COMMENT ON TABLE rush_orders IS 'Rush order details';

-- =====================================================
-- END OF SCHEMA
-- =====================================================