ALTER TABLE identities ALTER COLUMN id NOT NULL;
ALTER TABLE identities ALTER COLUMN email NOT NULL;
ALTER TABLE identities ALTER COLUMN fullname NOT NULL;
ALTER TABLE identities ADD PRIMARY KEY (id);
ALTER TABLE identities ADD UNIQUE (email, fullname);
