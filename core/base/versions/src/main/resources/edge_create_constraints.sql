ALTER TABLE edges ALTER COLUMN depot_id NOT NULL;
ALTER TABLE edges ALTER COLUMN source_id NOT NULL;
ALTER TABLE edges ALTER COLUMN target_id NOT NULL;
ALTER TABLE edges ADD UNIQUE(source_id, target_id);
