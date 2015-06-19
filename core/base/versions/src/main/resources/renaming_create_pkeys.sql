ALTER TABLE renamings ALTER COLUMN id NOT NULL;
ALTER TABLE renamings ALTER COLUMN source_id NOT NULL;
ALTER TABLE renamings ADD PRIMARY KEY (id, source_id);
