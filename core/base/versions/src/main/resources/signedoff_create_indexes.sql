CREATE INDEX idx_signedoffs_identity_id ON signedoffs (identity_id);
CREATE INDEX idx_signedoffs_changeset_id ON signedoffs (changeset_id);
CREATE INDEX idx_signedoffs_identity_id_changeset_id ON signedoffs (identity_id, changeset_id);
