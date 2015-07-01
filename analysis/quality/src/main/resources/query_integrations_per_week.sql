SELECT DISTINCT ON (h.id, csc.commit_time, csc.id) h.id handle_id, csc.commit_time commit_time
FROM branches b
INNER JOIN convergences cv ON (cv.branch_id = b.id) -- integration points
INNER JOIN changesets css ON (css.id = cv.source_id) -- source changeset
INNER JOIN changesets csc ON (csc.id = cv.converge_id) -- integration changeset
INNER JOIN revisions r ON (css.id = r.changeset_id)
INNER JOIN handles h ON (h.id = r.target_id) -- this needs to be target, not source_id because of copy detection. thus one changeset might have multiple revisions with the same source_id
INNER JOIN depots d ON (css.depot_id = d.id) 
WHERE b.name = 'master' -- only work on master branch
 AND d.name !~ 'external|kernel|device_' -- filter depots that contain external or kernel
AND (
	   r.change_type = 1 
	OR r.change_type = 4) -- 1 = ADDED, 4 = MODIFIED
AND (
	   r.lines_in > 0 
	OR r.lines_out > 0
) -- only text content changes
 AND h.path ~ '\.(c|cc|cpp|cxx|h|hh|hxx|java|py|pl|rb|js|cs)$' -- only source files
 AND css.commit_time BETWEEN '2006-01-01' AND d.mined -- sanitize invalid timestamp
 AND csc.commit_time BETWEEN '2006-01-01' AND d.mined
 AND css.authored_time BETWEEN '2006-01-01' AND d.mined
 AND csc.authored_time BETWEEN '2006-01-01' AND d.mined
-- no merges of changes older than a week
AND NOT EXISTS (
	SELECT 1
	FROM convergences cv2 
	INNER JOIN changesets css2 ON (css2.id = cv2.source_id)
	INNER JOIN changesets csc2 ON (csc2.id = cv2.converge_id)
	WHERE csc.id = csc2.id
	AND csc2.commit_time - css2.commit_time > '1 week'
)
--GROUP BY h.id, csc.id
ORDER BY h.id, csc.commit_time, csc.id  ASC 