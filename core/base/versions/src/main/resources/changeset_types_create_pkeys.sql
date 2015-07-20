ALTER TABLE changeset_types ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE changeset_types ADD PRIMARY KEY (changeset_id);
