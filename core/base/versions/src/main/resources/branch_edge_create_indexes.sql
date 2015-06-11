CREATE INDEX idx_branch_edges_depot_id ON branches USING btree (depot_id);
CREATE INDEX idx_branch_edges_name ON branches USING btree (name);
CREATE INDEX idx_branch_edges_type ON branches USING btree (type);
CREATE INDEX idx_branch_edges_depot_id_type ON branches USING btree (depot_id, type);
