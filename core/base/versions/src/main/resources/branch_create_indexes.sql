CREATE INDEX idx_branches_depot_id ON branches USING btree (depot_id);
CREATE INDEX idx_branches_id_depot_id ON branches USING btree (id, depot_id);