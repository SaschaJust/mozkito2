CREATE TABLE revisions (
	id BIGINT NOT NULL,
	changeset_id BIGINT NOT NULL,
	change_type SMALLINT NOT NULL,
	source_id BIGINT NOT NULL,
	target_id BIGINT NOT NULL,
	confidence SMALLINT NOT NULL,
	old_mode INT NOT NULL,
	new_mode INT NOT NULL,
	old_hash CHAR(40) NOT NULL,
	new_hash CHAR(40) NOT NULL,
	lines_in INT NOT NULL,
	lines_out INT NOT NULL
);
