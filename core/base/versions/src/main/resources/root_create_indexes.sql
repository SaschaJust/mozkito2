CREATE INDEX idx_roots_branch_id ON roots (branch_id);
CREATE INDEX idx_roots_changeset_id ON roots (changeset_id);
CREATE INDEX idx_roots_branch_id_changeset_id ON roots (branch_id, changeset_id);
