-- orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL NOT NULL,

    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,
    uuid VARCHAR(36) NOT NULL,

    number VARCHAR(32) NOT NULL,
    supplier VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,

    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

-- purchase_orders
CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGSERIAL NOT NULL,

    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    uuid VARCHAR(36) NOT NULL,
    number VARCHAR(32) NOT NULL,
    hash VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'WAITING',
    order_id BIGINT,

    CONSTRAINT purchase_orders_pkey PRIMARY KEY (id),
    CONSTRAINT purchase_orders_orders_fkey FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

-- orders_items
CREATE TABLE IF NOT EXISTS orders_items (
    id BIGSERIAL NOT NULL,

    inserted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,

    uuid VARCHAR(36) NOT NULL,
    ean VARCHAR(32) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    size VARCHAR(255),
    label_type VARCHAR(32) NOT NULL,
    order_id BIGINT,

    CONSTRAINT orders_items_pkey PRIMARY KEY (id),
    CONSTRAINT orders_items_orders_fkey FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE IF EXISTS orders_items CASCADE;
DROP SEQUENCE IF EXISTS orders_items_id_seq;

DROP TABLE IF EXISTS purchase_orders CASCADE;
DROP SEQUENCE IF EXISTS purchase_orders_id_seq;

DROP TABLE IF EXISTS orders CASCADE;
DROP SEQUENCE IF EXISTS orders_id_seq;
