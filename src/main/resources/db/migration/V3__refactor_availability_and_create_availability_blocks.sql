-- ============================================================
-- AVAILABILITIES
-- ============================================================

ALTER TABLE public.availabilities
DROP CONSTRAINT IF EXISTS availabilities_pkey;

ALTER TABLE public.availabilities
DROP COLUMN IF EXISTS id;

ALTER TABLE public.availabilities
    RENAME COLUMN uuid TO id;

ALTER TABLE public.availabilities
    ADD COLUMN enabled boolean NOT NULL DEFAULT true;

ALTER TABLE public.availabilities
    ADD COLUMN created_at timestamp(6) without time zone
        NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE public.availabilities
    ADD COLUMN updated_at timestamp(6) without time zone
        NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE public.availabilities
    ADD CONSTRAINT availabilities_pkey PRIMARY KEY (id);


-- ============================================================
-- AVAILABILITY BLOCKS
-- ============================================================

CREATE TABLE public.availability_blocks (
    id uuid NOT NULL,
    specialist_id uuid NOT NULL,
    starts_at timestamp(6) without time zone NOT NULL,
    ends_at timestamp(6) without time zone NOT NULL,
    reason varchar(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT availability_blocks_pkey PRIMARY KEY (id)
);