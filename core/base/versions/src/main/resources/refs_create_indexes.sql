CREATE INDEX idx_refs_depot_id ON refs (depot_id);
CREATE INDEX idx_refs_id_type ON refs (id, type);
CREATE INDEX idx_refs_id_type_depot_id ON refs (id, type, depot_id);
