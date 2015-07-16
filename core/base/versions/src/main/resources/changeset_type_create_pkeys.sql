ALTER TABLE changeset_integrationtypes ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE changeset_integrationtypes ADD PRIMARY KEY (changeset_id);
