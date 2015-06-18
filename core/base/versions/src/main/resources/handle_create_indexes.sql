CREATE INDEX idx_handles_depot_id ON handles (depot_id);
CREATE INDEX idx_handles_path ON handles (path);
CREATE INDEX idx_handles_depot_id_path ON handles (depot_id, path);
