ALTER TABLE changesets ADD FOREIGN KEY (author_id) REFERENCES identities (id);
ALTER TABLE changesets ADD FOREIGN KEY (committer_id) REFERENCES identities (id);
