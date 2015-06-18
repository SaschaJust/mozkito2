CREATE TABLE revisions (
	id BIGINT,
	depot_id BIGINT,
	changeset_id BIGINT,
	change_type SMALLINT,
	source_id BIGINT,
	target_id BIGINT,
	confidence SMALLINT,
	old_mode INT,
	new_mode INT,
	old_hash CHAR(40),
	new_hash CHAR(40),
	lines_in INT,
	lines_out INT
);
