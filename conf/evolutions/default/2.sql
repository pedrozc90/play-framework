# --- !Ups

-- users
CREATE TABLE IF NOT EXISTS users (
    id           BIGSERIAL    NOT NULL,
    -- audit
    uuid         VARCHAR(36)  NOT NULL,
    inserted_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      INTEGER      NOT NULL DEFAULT 0,

    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(32)  NOT NULL,
    active       BOOLEAN      NOT NULL DEFAULT true,

    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_uuid_ukey UNIQUE (uuid),
    CONSTRAINT users_email_ukey UNIQUE (email)
);

# --- !Downs
DROP TABLE IF EXISTS users;
