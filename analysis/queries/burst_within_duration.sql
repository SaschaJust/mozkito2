SELECT f.id, c1.id, COUNT(t.id)
FROM changesets c1
INNER JOIN revisions r1 ON (c1.id = r1.changeset_id)
INNER JOIN files f1 ON (r1.source_id = f1.handle_id OR r1.target_id = f1.handle_id),
(
	SELECT c2.id
	FROM changesets c2
	INNER JOIN revisions r2 ON (c2.id = r2.changeset_id)
	INNER JOIN files f2 ON (r2.source_id = f2.handle_id OR r2.target_id = f2.handle_id)
	WHERE c2.authored_time - c1.authored_time < '1 day'
	AND r2.change_type = 4 -- MODIFIED
) AS t
WHERE r1.change_type = 4 -- MODIFIED
GROUP BY f.id, c1.id 