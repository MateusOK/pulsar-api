-- V5__create_partner_benefit_structure.sql
-- Partner benefits / corporate discount structure

-- =========================================================
-- partners
-- =========================================================
CREATE TABLE partners (
    id          UUID         PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================================
-- partner_discounts
-- =========================================================
CREATE TABLE partner_discounts (
    id          UUID         PRIMARY KEY,
    partner_id  UUID         NOT NULL,
    percentage  NUMERIC(5,2) NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    expires_at  TIMESTAMPTZ  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_partner_discounts_partner
        FOREIGN KEY (partner_id)
        REFERENCES partners(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_partner_discounts_percentage
        CHECK (percentage >= 0 AND percentage <= 100)
);

-- Only one discount may be administratively active for a partner at a time.
-- Expiration is still validated by expires_at in the application/domain;
-- the expiration job should set active = false when appropriate.
CREATE UNIQUE INDEX uk_partner_discounts_one_active_per_partner
    ON partner_discounts (partner_id)
    WHERE active = TRUE;

CREATE INDEX idx_partner_discounts_partner_id
    ON partner_discounts (partner_id);

CREATE INDEX idx_partner_discounts_active_expires_at
    ON partner_discounts (active, expires_at);

-- =========================================================
-- partner_eligibility
-- =========================================================
CREATE TABLE partner_eligibility (
    id                   UUID         PRIMARY KEY,
    partner_discount_id  UUID         NOT NULL,
    type                 VARCHAR(20)  NOT NULL,
    value                VARCHAR(320) NOT NULL,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_partner_eligibility_discount
        FOREIGN KEY (partner_discount_id)
        REFERENCES partner_discounts(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_partner_eligibility_type
        CHECK (type IN ('EMAIL', 'DOMAIN'))
);

-- Same eligibility rule should not be registered twice for the same discount.
-- lower(value) makes the uniqueness case-insensitive.
CREATE UNIQUE INDEX uk_partner_eligibility_discount_type_value
    ON partner_eligibility (partner_discount_id, type, LOWER(value));

-- Supports lookup by normalized email/domain and eligibility type.
CREATE INDEX idx_partner_eligibility_type_value
    ON partner_eligibility (type, LOWER(value));

CREATE INDEX idx_partner_eligibility_discount_id
    ON partner_eligibility (partner_discount_id);

-- =========================================================
-- partner_discount_codes
-- =========================================================
CREATE TABLE partner_discount_codes (
    id                   UUID         PRIMARY KEY,
    partner_discount_id  UUID         NOT NULL,
    client_id            UUID         NOT NULL,
    recipient_email      VARCHAR(320) NOT NULL,
    code_hash            VARCHAR(64)  NOT NULL,
    status               VARCHAR(20)  NOT NULL,
    expires_at           TIMESTAMPTZ  NOT NULL,
    redeemed_at          TIMESTAMPTZ,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_partner_discount_codes_discount
        FOREIGN KEY (partner_discount_id)
        REFERENCES partner_discounts(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_partner_discount_codes_client
        FOREIGN KEY (client_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_partner_discount_codes_code_hash
        UNIQUE (code_hash),

    CONSTRAINT ck_partner_discount_codes_status
        CHECK (status IN ('ACTIVE', 'REDEEMED', 'EXPIRED', 'INVALIDATED')),

    CONSTRAINT ck_partner_discount_codes_redeemed_at
        CHECK (
            (status = 'REDEEMED' AND redeemed_at IS NOT NULL)
            OR
            (status <> 'REDEEMED')
        )
);

-- At most one ACTIVE code for the same client + discount.
CREATE UNIQUE INDEX uk_partner_discount_codes_one_active
    ON partner_discount_codes (client_id, partner_discount_id)
    WHERE status = 'ACTIVE';

-- Supports HMAC lookup during redemption.
CREATE INDEX idx_partner_discount_codes_client_hash
    ON partner_discount_codes (client_id, code_hash);

CREATE INDEX idx_partner_discount_codes_client_discount
    ON partner_discount_codes (client_id, partner_discount_id);

CREATE INDEX idx_partner_discount_codes_status_expires_at
    ON partner_discount_codes (status, expires_at);

-- =========================================================
-- client_partner_benefits
-- =========================================================
CREATE TABLE client_partner_benefits (
    id            UUID        PRIMARY KEY,
    client_id     UUID        NOT NULL,
    partner_id    UUID        NOT NULL,
    activated_at  TIMESTAMPTZ NOT NULL,
    active        BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_client_partner_benefits_client
        FOREIGN KEY (client_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_client_partner_benefits_partner
        FOREIGN KEY (partner_id)
        REFERENCES partners(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_client_partner_benefits_client_partner
        UNIQUE (client_id, partner_id)
);

CREATE INDEX idx_client_partner_benefits_client_active
    ON client_partner_benefits (client_id, active);

CREATE INDEX idx_client_partner_benefits_partner_id
    ON client_partner_benefits (partner_id);
