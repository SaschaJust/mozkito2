ALTER TABLE changeset_integrationtypes ALTER COLUMN changeset_id NOT NULL;
ALTER TABLE changeset_integrationtypes ALTER COLUMN integration_type NOT NULL;
ALTER TABLE changeset_integrationtypes ADD PRIMARY KEY (changeset_id);
ALTER TABLE changeset_integrationtypes ADD FOREIGN KEY (changeset_id) REFERENCES changesets (id);