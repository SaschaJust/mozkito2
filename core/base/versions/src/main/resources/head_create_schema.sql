CREATE SEQUENCE seq_heads_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE heads (
	id BIGINT PRIMARY KEY,
	branch_id BIGINT,
	changeset_id BIGINT
);