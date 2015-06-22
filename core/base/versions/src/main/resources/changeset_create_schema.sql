CREATE TABLE changesets (
	id BIGINT NOT NULL,
	depot_id BIGINT NOT NULL,
	commit_hash CHAR(40) NOT NULL,
	tree_hash CHAR(40) NOT NULL,
	authored_time TIMESTAMP NOT NULL,
	author_id BIGINT NOT NULL,
	commit_time TIMESTAMP NOT NULL,
	committer_id BIGINT NOT NULL,
	subject VARCHAR(900) NOT NULL,
	body VARCHAR(900) NOT NULL
);
