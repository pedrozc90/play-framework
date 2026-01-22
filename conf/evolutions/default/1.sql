# --- !Ups
-- create pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- file storage
CREATE TABLE IF NOT EXISTS file_storage (
    id           BIGSERIAL    NOT NULL,
    -- audit
    uuid         VARCHAR(36)  NOT NULL DEFAULT gen_random_uuid(),
    inserted_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      INTEGER      NOT NULL DEFAULT 0,

    hash         VARCHAR(64)  NOT NULL,
    filename     VARCHAR(255) NOT NULL,
    content      BYTEA        NOT NULL,
    content_type VARCHAR(64)  NOT NULL DEFAULT 'application/octet-stream',
    extension    VARCHAR(8)   NOT NULL,
    charset      VARCHAR(16),
    length       BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT file_storage_pkey PRIMARY KEY (id),
    CONSTRAINT file_storage_uuid_ukey UNIQUE (uuid)
);

-- image jobs
CREATE TABLE IF NOT EXISTS jobs (
    id          BIGSERIAL   NOT NULL,
    -- audit
    uuid        VARCHAR(36) NOT NULL DEFAULT gen_random_uuid(),
    inserted_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version     INTEGER     NOT NULL DEFAULT 0,

    status      VARCHAR(64) NOT NULL DEFAULT 'PENDING',
    file_id     BIGINT      NOT NULL,

    CONSTRAINT jobs_pkey PRIMARY KEY (id),
    CONSTRAINT jobs_uuid_ukey UNIQUE (uuid),
    CONSTRAINT jobs_file_id_fkey FOREIGN KEY (file_id) REFERENCES file_storage (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- image tasks
CREATE TABLE IF NOT EXISTS tasks (
    id           BIGSERIAL   NOT NULL,
    -- audit
    uuid         VARCHAR(36) NOT NULL DEFAULT gen_random_uuid(),
    inserted_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      INTEGER     NOT NULL DEFAULT 0,

    type         VARCHAR(64) NOT NULL,
    status       VARCHAR(64) NOT NULL DEFAULT 'PENDING',
    stack_trace  TEXT,
    started_at   TIMESTAMP,
    completed_at TIMESTAMP,

    job_id       BIGINT      NOT NULL,

    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT tasks_uuid_ukey UNIQUE (uuid),
    CONSTRAINT tasks_job_id_fkey FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- users
CREATE TABLE IF NOT EXISTS users (
    id           BIGSERIAL    NOT NULL,
    -- audit
    uuid         VARCHAR(36)  NOT NULL DEFAULT gen_random_uuid(),
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

INSERT INTO users (email, password)
VALUES ( 'contare@email.com', md5('password'));

# --- !Downs
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS file_storage CASCADE;
DROP TABLE IF EXISTS users;
