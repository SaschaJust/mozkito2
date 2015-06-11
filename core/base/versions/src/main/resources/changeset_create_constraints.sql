ALTER TABLE changesets ALTER COLUMN depot_id NOT NULL;
ALTER TABLE changesets ALTER COLUMN commit_hash NOT NULL;
ALTER TABLE changesets ALTER COLUMN tree_hash NOT NULL;
ALTER TABLE changesets ALTER COLUMN authored_time NOT NULL;
ALTER TABLE changesets ALTER COLUMN author_id NOT NULL;
ALTER TABLE changesets ALTER COLUMN commit_time NOT NULL;
ALTER TABLE changesets ALTER COLUMN committer_id NOT NULL;
ALTER TABLE changesets ALTER COLUMN subject NOT NULL;
ALTER TABLE changesets ALTER COLUMN body NOT NULL;

--ALTER TABLE ONLY changesets
--    ADD CONSTRAINT changesets_pkey PRIMARY KEY (id);

ALTER TABLE ONLY changesets
    ADD CONSTRAINT changesets_depot_id_commit_hash_key UNIQUE (depot_id, commit_hash);

ALTER TABLE ONLY changesets
    ADD CONSTRAINT changesets_depot_id_fkey FOREIGN KEY (depot_id) REFERENCES depots(id);
		
ALTER TABLE ONLY changesets
    ADD CONSTRAINT changesets_author_id_fkey FOREIGN KEY (author_id) REFERENCES identities(id);
	
ALTER TABLE ONLY changesets
    ADD CONSTRAINT changesets_committer_id_fkey FOREIGN KEY (committer_id) REFERENCES identities(id);
