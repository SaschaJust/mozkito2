CREATE TABLE changeset_integrationtypes (
	changeset_id BIGINT PRIMARY KEY,
	integration_type SMALLINT
);

CREATE TABLE integration_types (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(32)
);

