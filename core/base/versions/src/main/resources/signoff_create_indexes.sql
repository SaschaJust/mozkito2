CREATE INDEX idx_signoffs_identity_id ON signoffs (identity_id);
CREATE INDEX idx_signoffs_changeset_id ON signoffs (changeset_id);
CREATE INDEX idx_signoffs_identity_id_changeset_id ON signoffs (identity_id, changeset_id);
