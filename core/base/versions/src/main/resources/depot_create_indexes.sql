CREATE INDEX idx_depots_name ON depots USING btree (name);
CREATE INDEX idx_depots_origin ON depots USING btree (origin);