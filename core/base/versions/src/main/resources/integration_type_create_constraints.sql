ALTER TABLE integration_types ALTER COLUMN type NOT NULL;
ALTER TABLE integration_types ADD UNIQUE(type);