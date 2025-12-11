-- Restaurant Service Database Initialization

-- ==========================================
-- AGGREGATE 1: Restaurant
-- ==========================================

-- Create Restaurant table (Aggregate Root)
CREATE TABLE IF NOT EXISTS restaurants (
    restaurant_id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(255) NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address VARCHAR(500) NOT NULL,

    approval_status ENUM('Pending', 'Approved', 'Suspended') 
        NOT NULL DEFAULT 'Pending',

    operating_hours VARCHAR(500),  -- could be JSON or string representation

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Value Object Validation (RestaurantProfile)
    CONSTRAINT chk_restaurant_email CHECK (email LIKE '%@%'),
    CONSTRAINT chk_restaurant_phone CHECK (phone REGEXP '^[0-9+() -]{5,20}$'),

    INDEX idx_restaurant_name (name),
    INDEX idx_owner_name (owner_name),
    INDEX idx_approval_status (approval_status)
);

-- Common composite index for approval workflows
CREATE INDEX IF NOT EXISTS idx_restaurant_status_name 
    ON restaurants(approval_status, name);



-- ==========================================
-- AGGREGATE 2: Menu
-- ==========================================

-- Create Menu table (Aggregate Root)
CREATE TABLE IF NOT EXISTS menus (
    menu_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,

    version INT NOT NULL DEFAULT 1,
    status ENUM('Draft', 'Published', 'Archived') NOT NULL DEFAULT 'Draft',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,

    INDEX idx_restaurant_id (restaurant_id),
    INDEX idx_menu_status (status),
    INDEX idx_menu_version (version)
);


-- Create MenuItem table (Value Object)
CREATE TABLE IF NOT EXISTS menu_items (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    menu_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(10, 2) NOT NULL,

    category ENUM('Drinks', 'Meals', 'Desserts') NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (menu_id) REFERENCES menus(menu_id) ON DELETE CASCADE,

    -- Value Object Rules
    CONSTRAINT chk_price_positive CHECK (price > 0),
    CONSTRAINT chk_valid_category CHECK (category IN ('Drinks', 'Meals', 'Desserts')),

    INDEX idx_menu_id (menu_id),
    INDEX idx_category (category),
    INDEX idx_item_name (name)
);

-- Composite index for faster menu browsing
CREATE INDEX IF NOT EXISTS idx_menuitem_menu_category 
    ON menu_items(menu_id, category);
