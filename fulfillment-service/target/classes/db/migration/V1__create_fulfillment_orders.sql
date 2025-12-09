CREATE TABLE IF NOT EXISTS fulfillment_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    assigned_staff VARCHAR(255),
    estimated_minutes INT NOT NULL,
    special_instruction TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_fulfillment_order UNIQUE (order_id)
);

CREATE TABLE IF NOT EXISTS fulfillment_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fulfillment_order_id BIGINT NOT NULL,
    item VARCHAR(255) NOT NULL,
    CONSTRAINT fk_fulfillment_order_items_order FOREIGN KEY (fulfillment_order_id)
        REFERENCES fulfillment_orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_fulfillment_orders_restaurant ON fulfillment_orders (restaurant_id);
