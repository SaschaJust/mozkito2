CREATE SEQUENCE seq_convergences_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE convergences (
	id BIGINT PRIMARY KEY,
	branch_id BIGINT,
	source_id BIGINT,
	converge_id BIGINT
);