ALTER TABLE roots ALTER COLUMN branch_id NOT NULL;
ALTER TABLE roots ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE roots ADD UNIQUE (changeset_id, branch_id);
