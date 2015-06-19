ALTER TABLE renamings ALTER COLUMN similarity NOT NULL;
ALTER TABLE renamings ALTER COLUMN source_id NOT NULL;
ALTER TABLE renamings ALTER COLUMN target_id NOT NULL;
ALTER TABLE renamings ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE renamings ADD UNIQUE (changeset_id, source_id);
ALTER TABLE renamings ADD UNIQUE (changeset_id, target_id);