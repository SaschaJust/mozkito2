CREATE INDEX idx_changesets_author_id ON changesets USING btree (author_id);
CREATE INDEX idx_changesets_author_time ON changesets USING btree (authored_time);
CREATE INDEX idx_changesets_commit_time ON changesets USING btree (commit_time);
CREATE INDEX idx_changesets_committer_id ON changesets USING btree (committer_id);
CREATE INDEX idx_changesets_depot_id ON changesets USING btree (depot_id);
CREATE INDEX idx_changesets_tree_hash ON changesets USING btree (tree_hash);