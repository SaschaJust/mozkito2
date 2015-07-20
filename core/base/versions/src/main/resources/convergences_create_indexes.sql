CREATE INDEX idx_convergence_branch_id ON convergences (branch_id);
CREATE INDEX idx_convergence_source_id ON convergences (source_id);
CREATE INDEX idx_convergence_converge_id ON convergences (converge_id);
CREATE INDEX idx_convergence_branch_id_source_id ON convergences (branch_id, source_id);
CREATE INDEX idx_convergence_branch_id_converge_id ON convergences (branch_id, converge_id);
