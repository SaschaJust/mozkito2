ALTER TABLE heads ALTER COLUMN branch_id NOT NULL;
ALTER TABLE heads ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE heads ADD UNIQUE (branch_id, changeset_id);