CREATE SEQUENCE seq_roots_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE roots (
	id BIGINT,
	branch_id BIGINT,
	changeset_id BIGINT
);
