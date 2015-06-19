ALTER TABLE branches ALTER COLUMN depot_id NOT NULL;
ALTER TABLE branches ALTER COLUMN name NOT NULL;
ALTER TABLE branches ADD UNIQUE (depot_id, name);
