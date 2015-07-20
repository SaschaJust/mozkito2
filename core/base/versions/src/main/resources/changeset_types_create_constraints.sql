ALTER TABLE changeset_types ADD FOREIGN KEY (changeset_id) REFERENCES changesets (id);
