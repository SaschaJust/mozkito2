CREATE SEQUENCE seq_changesets_id MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE changesets (
	id bigint PRIMARY KEY,
	depot_id smallint,
	commit_hash uuid,
	commit_hash_crc int,
	origin int, 
	original_id varchar(40) DEFAULT NULL,
	branch_parent_hash uuid DEFAULT NULL,
	branch_parent_crc uuid DEFAULT NULL,
	tree_hash uuid,
	tree_hash_crc int,
	patch_hash uuid,
	patch_hash_crc int,
	authored_time timestamp,
	author_id int,
	commit_time timestamp,
	committer_id int
);

CREATE TABLE changeset_merge_parents (
	changeset_id bigint,
	parent_id bigint,
);

CREATE TABLE changeset_branches (
	changeset_id bigint,
	branch_id int,	
);
