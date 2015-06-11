CREATE INDEX idx_revisions_change_type ON revisions USING btree (change_type);
CREATE INDEX idx_revisions_changeset_id ON revisions USING btree (changeset_id);
CREATE INDEX idx_revisions_depot_id ON revisions USING btree (depot_id);
CREATE INDEX idx_revisions_source_id ON revisions USING btree (source_id);
CREATE INDEX idx_revisions_target_id ON revisions USING btree (target_id);
CREATE INDEX idx_revisions_source_id_target_id ON revisions USING btree (source_id, target_id);