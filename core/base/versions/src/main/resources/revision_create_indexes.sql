CREATE INDEX idx_revisions_depot_id ON revisions (depot_id);
CREATE INDEX idx_revisions_changeset_id ON revisions (changeset_id);
CREATE INDEX idx_revisions_change_type ON revisions (change_type);
CREATE INDEX idx_revisions_source_id ON revisions (source_id);
CREATE INDEX idx_revisions_target_id ON revisions (target_id);
CREATE INDEX idx_revisions_confidence ON revisions (confidence);
CREATE INDEX idx_revisions_old_mode ON revisions (old_mode);
CREATE INDEX idx_revisions_new_mode ON revisions (new_mode);
CREATE INDEX idx_revisions_old_hash ON revisions (old_hash);
CREATE INDEX idx_revisions_new_hash ON revisions (new_hash);
CREATE INDEX idx_revisions_lines_in ON revisions (lines_in);
CREATE INDEX idx_revisions_lines_out ON revisions (lines_out);
CREATE INDEX idx_revisions_source_id_target_id ON revisions (source_id, target_id);
CREATE INDEX idx_revisions_lines_in_lines_out ON revisions (lines_in, lines_out);
CREATE INDEX idx_revisions_old_mode_new_mode ON revisions (old_mode, new_mode);
CREATE INDEX idx_revisions_old_hash_new_hash ON revisions (old_hash, new_hash);