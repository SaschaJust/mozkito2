SELECT DISTINCT ON (h.id, csc.commit_time, csc.id) h.id handle_id, csc.commit_time commit_time
FROM branches b
INNER JOIN convergences cv ON (cv.branch_id = b.id AND b.name = 'master') -- integration points
INNER JOIN changesets css ON (css.id = cv.source_id) -- source changeset
INNER JOIN changesets csc ON (csc.id = cv.converge_id) -- integration changeset
INNER JOIN changeset_integrationtypes cits ON ( css.id = cits.changeset_id AND (cits.integration_type & 1 <> 0)) -- edits no merges
INNER JOIN revisions r ON (css.id = r.changeset_id)
INNER JOIN handles h ON (h.id = r.target_id) -- this needs to be target, not source_id because of copy detection. thus one changeset might have multiple revisions with the same source_id
INNER JOIN depots d ON (b.depot_id = d.id) 
INNER JOIN depot_contributions dc ON (d.id = dc.depot_id AND dc.google_contribution >= 25)
INNER JOIN changeset_sizes csize ON (css.id = csize.changeset_id) 
INNER JOIN integration_times intt ON (intt.id = csc.id)
INNER JOIN identities i ON (css.author_id = i.id)
WHERE b.name = 'master' -- only work on master branch
--AND b.type = 1 -- only branches, no tags // not available in old DB layout. b only contains branches there
AND csize.num_files < 100 -- no drops (less than 100 files touched)
AND intt.max < '1 month' -- no integrations that contain changes older than a month
AND ( -- file revisions
	   r.change_type = 1  -- 1 = ADDED
	OR r.change_type = 4) -- 4 = MODIFIED
AND ( -- only text content changes
	   r.lines_in > 0 
	OR r.lines_out > 0
) 
AND NOT ( -- community emails approximation
	   i.email LIKE '%@google.com'
	OR i.email LIKE '%@android.com'
)
AND (  -- only source files
	   h.path ILIKE '%.c' 
	OR h.path ILIKE '%.h'
	OR h.path ILIKE '%.cc'
	OR h.path ILIKE '%.cpp'
	OR h.path ILIKE '%.cxx'
	OR h.path ILIKE '%.hh'
	OR h.path ILIKE '%.hxx'
	OR h.path ILIKE '%.java'
	OR h.path ILIKE '%.py'
	OR h.path ILIKE '%.pl'
	OR h.path ILIKE '%.rb'
	OR h.path ILIKE '%.js'
	OR h.path ILIKE '%.cs'
)
AND h.path NOT ILIKE '%TEST%' -- no tests
AND css.commit_time BETWEEN '2008-09-01' AND d.mined -- sanitize invalid timestamp
AND csc.commit_time BETWEEN '2008-09-01' AND d.mined
AND css.authored_time BETWEEN '2008-09-01' AND d.mined
AND csc.authored_time BETWEEN '2008-09-01' AND d.mined
ORDER BY h.id, csc.commit_time, csc.id  ASC 
