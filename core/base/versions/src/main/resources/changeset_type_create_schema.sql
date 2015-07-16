CREATE TABLE changeset_types (
	changeset_id BIGINT NOT NULL,
	integration_type SMALLINT NOT NULL
);

CREATE TABLE integration_types (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(32) NOT NULL
);


