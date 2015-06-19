ALTER TABLE identities ALTER COLUMN email NOT NULL;
ALTER TABLE identities ALTER COLUMN fullname NOT NULL;
ALTER TABLE identities ADD UNIQUE (email, fullname);
