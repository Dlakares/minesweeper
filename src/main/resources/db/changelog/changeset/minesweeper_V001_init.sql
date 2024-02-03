CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS fields (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    width int NOT NULL,
    height int NULL,
    mines_count int NOT NULL,
    field varchar[][] NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS games (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    field_id BIGINT NOT NULL,
    result boolean DEFAULT false NOT NULL,
    started_at timestamptz DEFAULT current_timestamp,
    ended_at timestamptz,

    CONSTRAINT fk_field_id FOREIGN KEY (field_id) REFERENCES fields (id)
);