ALTER TABLE convergences ALTER COLUMN id NOT NULL;
ALTER TABLE convergences ALTER COLUMN branch_id NOT NULL;
ALTER TABLE convergences ALTER COLUMN source_id NOT NULL;
ALTER TABLE convergences ALTER COLUMN converge_id NOT NULL;
ALTER TABLE convergences ADD PRIMARY KEY (id);
ALTER TABLE convergences ADD UNIQUE (branch_id, source_id);
