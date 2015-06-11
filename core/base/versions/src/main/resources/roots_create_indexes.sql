CREATE INDEX idx_roots_branch_id ON roots USING btree (branch_id);
CREATE INDEX idx_roots_changeset_id ON roots USING btree (changeset_id);
CREATE INDEX idx_roots_branch_id_changeset_id ON roots USING btree (branch_id, changeset_id);