CREATE SEQUENCE seq_branch_edges_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE branch_edges (
	id BIGINT PRIMARY KEY,
	edge_id BIGINT,
	branch_id BIGINT,
	branch_type SMALLINT,
	navigation_type SMALLINT,
	integration_type SMALLINT
);
