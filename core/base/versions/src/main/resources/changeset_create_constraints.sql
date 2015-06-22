ALTER TABLE changesets ADD FOREIGN KEY (depot_id) REFERENCES depots (id);
ALTER TABLE changesets ADD FOREIGN KEY (author_id) REFERENCES identities (id);
ALTER TABLE changesets ADD FOREIGN KEY (committer_id) REFERENCES identities (id);
ALTER TABLE changesets ADD UNIQUE (depot_id, commit_hash);
