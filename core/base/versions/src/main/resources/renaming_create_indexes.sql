CREATE INDEX idx_renamings_source_id ON renamings (source_id);
CREATE INDEX idx_renamings_target_id ON renamings (target_id);
CREATE INDEX idx_renamings_changeset_id ON renamings (changeset_id);
CREATE INDEX idx_renamings_source_id_target_id ON renamings (source_id, target_id);
CREATE INDEX idx_renamings_similarity ON renamings (similarity);
