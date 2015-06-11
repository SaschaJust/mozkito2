CREATE INDEX changesets_author_id_idx ON changesets USING btree (author_id);
CREATE INDEX changesets_author_time_idx ON changesets USING btree (authored_time);
CREATE INDEX changesets_commit_time_idx ON changesets USING btree (commit_time);
CREATE INDEX changesets_committer_id_idx ON changesets USING btree (committer_id);
CREATE INDEX changesets_depot_id_idx ON changesets USING btree (depot_id);
CREATE INDEX changesets_tree_hash_idx ON changesets USING btree (tree_hash);