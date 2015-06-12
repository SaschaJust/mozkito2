CREATE INDEX idx_branch_edges_edge_id ON branch_edges (edge_id);
CREATE INDEX idx_branch_edges_branch_id ON branch_edges (branch_id);
CREATE INDEX idx_branch_edges_type ON branch_edges (type);
CREATE INDEX idx_branch_edges_edge_id_type ON branch_edges (edge_id, type);
CREATE INDEX idx_branch_edges_branch_id_type ON branch_edges (branch_id, type);
CREATE INDEX idx_branch_edges_branch_id_edge_id ON branch_edges (branch_id, edge_id);
CREATE INDEX idx_branch_edges_branch_id_edge_id_type ON branch_edges (branch_id, edge_id, type);
