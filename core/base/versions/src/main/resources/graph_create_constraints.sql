ALTER TABLE graphs ALTER COLUMN depot_id NOT NULL;
ALTER TABLE graphs ADD UNIQUE (depot_id);