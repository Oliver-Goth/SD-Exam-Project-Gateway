ALTER TABLE fulfillment_orders
ADD COLUMN customer_id BIGINT NULL;

CREATE INDEX idx_fulfillment_orders_customer
    ON fulfillment_orders (customer_id);
