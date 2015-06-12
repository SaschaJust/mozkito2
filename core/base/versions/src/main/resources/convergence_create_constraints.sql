ALTER TABLE convergences ALTER COLUMN branch_id NOT NULL;
ALTER TABLE convergences ALTER COLUMN source_id NOT NULL;
ALTER TABLE convergences ALTER COLUMN convergence_id NOT NULL;
ALTER TABLE convergences ADD UNIQUE (branch_id, source_id);
