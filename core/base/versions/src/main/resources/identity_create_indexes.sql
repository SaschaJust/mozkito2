CREATE INDEX idx_identities_email ON identities USING btree (email);
CREATE INDEX idx_identities_fullname ON identities USING btree (fullname);