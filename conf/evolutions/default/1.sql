# --- !Ups
-- create pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- suppliers
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGSERIAL NOT NULL,

    uuid VARCHAR(36) NOT NULL,
    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    code VARCHAR(32) NOT NULL,

    CONSTRAINT suppliers_pkey PRIMARY KEY (id),
    CONSTRAINT suppliers_code_key UNIQUE (code)
);

-- products
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL NOT NULL,

    uuid VARCHAR(36) NOT NULL,
    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    ean VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    size VARCHAR(64),
    color VARCHAR(64),

    CONSTRAINT products_pkey PRIMARY KEY (id)
);

-- orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL NOT NULL,

    uuid VARCHAR(36) NOT NULL,
    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    number VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,

    supplier_id BIGINT NOT NULL,

    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT orders_suppliers_fkey FOREIGN KEY (supplier_id) REFERENCES suppliers (id) ON DELETE CASCADE
);

-- orders_items
CREATE TABLE IF NOT EXISTS orders_items (
    id BIGSERIAL NOT NULL,

    uuid VARCHAR(36) NOT NULL,
    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    quantity INTEGER NOT NULL DEFAULT 0,
    label_type VARCHAR(32) NOT NULL,
    cancelled boolean NOT NULL DEFAULT false,

    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    CONSTRAINT orders_items_pkey PRIMARY KEY (id),
    CONSTRAINT orders_items_order_fkey FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT orders_items_product_fkey FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

-- purchase_orders
CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGSERIAL NOT NULL,

    uuid VARCHAR(36) NOT NULL,
    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    number VARCHAR(32) NOT NULL,
    hash VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'WAITING',
    changelog TEXT,

    order_id BIGINT,

    CONSTRAINT purchase_orders_pkey PRIMARY KEY (id),
    CONSTRAINT purchase_orders_order_fkey FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE IF EXISTS purchase_orders CASCADE;
DROP SEQUENCE IF EXISTS purchase_orders_id_seq;

DROP TABLE IF EXISTS orders_items CASCADE;
DROP SEQUENCE IF EXISTS orders_items_id_seq;

DROP TABLE IF EXISTS orders CASCADE;
DROP SEQUENCE IF EXISTS orders_id_seq;

DROP TABLE IF EXISTS products CASCADE;
DROP SEQUENCE IF EXISTS products_id_seq;

DROP TABLE IF EXISTS suppliers CASCADE;
DROP SEQUENCE IF EXISTS suppliers_id_seq;
