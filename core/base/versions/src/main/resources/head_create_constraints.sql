ALTER TABLE heads ALTER COLUMN id NOT NULL;
ALTER TABLE heads ALTER COLUMN branch_id NOT NULL;
ALTER TABLE heads ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE heads ADD PRIMARY KEY (id);
ALTER TABLE heads ADD UNIQUE (branch_id, changeset_id);