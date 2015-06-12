CREATE INDEX idx_revisions_change_type ON revisions (change_type);
CREATE INDEX idx_revisions_changeset_id ON revisions (changeset_id);
CREATE INDEX idx_revisions_depot_id ON revisions (depot_id);
CREATE INDEX idx_revisions_source_id ON revisions (source_id);
CREATE INDEX idx_revisions_target_id ON revisions (target_id);
CREATE INDEX idx_revisions_source_id_target_id ON revisions (source_id, target_id);
