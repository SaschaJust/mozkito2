CREATE INDEX idx_changesets_author_id ON changesets (author_id);
CREATE INDEX idx_changesets_author_time ON changesets (authored_time);
CREATE INDEX idx_changesets_commit_time ON changesets (commit_time);
CREATE INDEX idx_changesets_committer_id ON changesets (committer_id);
CREATE INDEX idx_changesets_tree_hash ON changesets (tree_hash);
CREATE INDEX idx_changesets_commit_hash ON changesets (commit_hash);
