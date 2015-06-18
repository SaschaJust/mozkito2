ALTER TABLE integration_types ALTER COLUMN id NOT NULL;
ALTER TABLE integration_types ALTER COLUMN type NOT NULL;
ALTER TABLE integration_types ADD PRIMARY KEY (id); 
ALTER TABLE integration_types ADD UNIQUE(type);