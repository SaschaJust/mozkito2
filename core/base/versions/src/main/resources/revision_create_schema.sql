CREATE SEQUENCE seq_revisions_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE revisions (
	id BIGINT PRIMARY KEY,
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
