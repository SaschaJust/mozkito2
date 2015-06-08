CREATE OR REPLACE FUNCTION createFiles() RETURNS VOID
AS $$
DECLARE
   a BIGINT;
BEGIN
	DROP SEQUENCE IF EXISTS seq_files;
	CREATE SEQUENCE seq_files; 
	FOR a IN
		SELECT target_id AS a FROM revisions
		WHERE change_type = 1
	LOOP 
		EXECUTE 
		'INSERT INTO files
		WITH RECURSIVE f(s, t, i) AS (
		SELECT source_id, target_id, NEXTVAL(''seq_files'')
		FROM revisions r
		WHERE r.change_type = 1  -- ADD
		AND r.id = ' || a || '
		UNION ALL
		SELECT source_id, target_id, f.i
		FROM f, revisions r
		WHERE f.t = r.source_id
		AND r.target_id <> f.s
		AND r.change_type = 8 -- RENAME
		)
		SELECT i, t FROM f;';
	END LOOP;
END
$$ LANGUAGE plpgsql;


