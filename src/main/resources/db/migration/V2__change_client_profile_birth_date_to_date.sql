ALTER TABLE client_profiles
ALTER COLUMN date_of_birth TYPE DATE
USING date_of_birth::DATE;