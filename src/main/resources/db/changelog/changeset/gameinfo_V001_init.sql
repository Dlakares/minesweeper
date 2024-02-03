CREATE TABLE IF NOT EXISTS gameinfo (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    width int NOT NULL,
    height int NOT NULL,
    mines_count int NOT NULL,
    field varchar[][] NOT NULL,
    started_at timestamptz DEFAULT current_timestamp,
    ended_at timestamptz DEFAULT current_timestamp
)