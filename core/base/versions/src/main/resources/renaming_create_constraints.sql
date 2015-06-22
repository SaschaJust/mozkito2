ALTER TABLE renamings ADD UNIQUE (changeset_id, source_id);
ALTER TABLE renamings ADD UNIQUE (changeset_id, target_id);
