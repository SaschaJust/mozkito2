CREATE INDEX idx_convergence_branch_id ON convergence USING btree (branch_id);
CREATE INDEX idx_convergence_source_id ON convergence USING btree (source_id);
CREATE INDEX idx_convergence_converge_id ON convergence USING btree (converge_id);
CREATE INDEX idx_convergence_branch_id_source_id ON convergence USING btree (branch_id, source_id);
CREATE INDEX idx_convergence_branch_id_converge_id ON convergence USING btree (branch_id, converge_id);
