INSERT INTO changesets (
		id,
		commit_hash,
		tree_hash,
		authored_time,
		author_id,
		commit_time,
		committer_id,
		subject,
		body
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)