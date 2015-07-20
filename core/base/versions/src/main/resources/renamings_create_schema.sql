CREATE TABLE renamings (
	id BIGINT NOT NULL,
	similarity SMALLINT NOT NULL,
	source_id BIGINT NOT NULL,
	target_id BIGINT NOT NULL,
	changeset_id BIGINT NOT NULL
);
