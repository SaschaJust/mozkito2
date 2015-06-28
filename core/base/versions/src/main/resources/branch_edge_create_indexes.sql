CREATE INDEX idx_branch_edges_edge_id ON branch_edges (edge_id);
CREATE INDEX idx_branch_edges_branch_id ON branch_edges (branch_id);
CREATE INDEX idx_branch_edges_navigation_type ON branch_edges (navigation_type);
CREATE INDEX idx_branch_edges_integration_type ON branch_edges (integration_type);
CREATE INDEX idx_branch_edges_edge_id_navigation_type ON branch_edges (edge_id, navigation_type);
CREATE INDEX idx_branch_edges_edge_id_integration_type ON branch_edges (edge_id, integration_type);
CREATE INDEX idx_branch_edges_branch_id_edge_id ON branch_edges (branch_id, edge_id);
CREATE INDEX idx_branch_edges_branch_id_edge_id_navigation_type ON branch_edges (branch_id, edge_id, navigation_type);
CREATE INDEX idx_branch_edges_branch_id_edge_id_integration_type ON branch_edges (branch_id, edge_id, integration_type);
