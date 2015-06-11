CREATE INDEX idx_heads_branch_id ON heads USING btree (branch_id);
CREATE INDEX idx_heads_changeset_id ON heads USING btree (changeset_id);
CREATE INDEX idx_heads_branch_id_changeset_id ON heads USING btree (branch_id, changeset_id);