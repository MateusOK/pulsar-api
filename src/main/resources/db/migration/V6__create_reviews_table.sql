-- V6__create_reviews_table.sql

CREATE TABLE reviews (
    id            UUID          PRIMARY KEY,
    specialist_id UUID          NOT NULL,
    client_id     UUID          NOT NULL,
    comment       VARCHAR(280)  NOT NULL,
    rating        NUMERIC(3,2)  NOT NULL,
    status        VARCHAR(20)   NOT NULL,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reviews_specialist
        FOREIGN KEY (specialist_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_reviews_client
        FOREIGN KEY (client_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_reviews_rating
        CHECK (rating >= 0 AND rating <= 5),

    CONSTRAINT ck_reviews_status
        CHECK (status IN ('PENDING', 'PUBLISHED', 'REJECTED'))
);

CREATE INDEX idx_reviews_specialist_status_created_at
    ON reviews (specialist_id, status, created_at DESC);

CREATE UNIQUE INDEX uq_reviews_published_client_specialist
    ON reviews (client_id, specialist_id)
    WHERE status = 'PUBLISHED';
