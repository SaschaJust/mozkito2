CREATE INDEX idx_heads_branch_id ON heads (branch_id);
CREATE INDEX idx_heads_changeset_id ON heads (changeset_id);
CREATE INDEX idx_heads_branch_id_changeset_id ON heads (branch_id, changeset_id);
