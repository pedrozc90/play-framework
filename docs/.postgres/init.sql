-- script used on docker-compose.yml initialization

-- create a user with a password (optional but recommended)
CREATE ROLE play WITH LOGIN PASSWORD 'play' SUPERUSER;

-- create databases
CREATE DATABASE play OWNER play;
CREATE DATABASE play_test OWNER play;

-- optional: connect to each DB and grant full privileges
\connect play
GRANT ALL PRIVILEGES ON DATABASE play TO play;
GRANT ALL ON SCHEMA public TO play;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO play;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO play;

\connect play_test
GRANT ALL PRIVILEGES ON DATABASE play_test TO play;
GRANT ALL ON SCHEMA public TO play;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO play;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO play;
