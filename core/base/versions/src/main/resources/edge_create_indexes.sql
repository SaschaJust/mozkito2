CREATE INDEX idx_edges_id_depot_id ON edges (id, depot_id);
CREATE INDEX idx_edges_depot_id ON edges (depot_id);
CREATE INDEX idx_edges_source_id ON edges (source_id);
CREATE INDEX idx_edges_target_id ON edges (target_id);
CREATE INDEX idx_edges_depot_id_source_id ON edges (depot_id, source_id);
CREATE INDEX idx_edges_depot_id_target_id ON edges (depot_id, target_id);
