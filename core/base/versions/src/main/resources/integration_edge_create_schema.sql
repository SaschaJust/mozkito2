CREATE SEQUENCE seq_integration_edges_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE integration_edges (
	id BIGINT PRIMARY KEY,
	edge_id BIGINT,
	branch_id BIGINT
);