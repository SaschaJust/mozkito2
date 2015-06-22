CREATE TABLE branch_edges (
	id BIGINT NOT NULL,
	edge_id BIGINT NOT NULL,
	branch_id BIGINT NOT NULL,
	branch_type SMALLINT NOT NULL,
	navigation_type SMALLINT NOT NULL,
	integration_type SMALLINT NOT NULL
);
