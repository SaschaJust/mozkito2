ALTER TABLE branch_edges ALTER COLUMN edge_id NOT NULL;
ALTER TABLE branch_edges ALTER COLUMN branch_id NOT NULL;
ALTER TABLE branch_edges ALTER COLUMN branch_type NOT NULL;
ALTER TABLE branch_edges ALTER COLUMN navigation_type NOT NULL;
ALTER TABLE branch_edges ALTER COLUMN integration_type NOT NULL;
ALTER TABLE branch_edges ADD UNIQUE (branch_id, edge_id);
