ALTER TABLE changeset_integrationtypes ADD FOREIGN KEY (changeset_id) REFERENCES changesets (id);
