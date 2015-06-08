SELECT t.email, MAX(t.contrib)::decimal(5,2) m FROM (
	SELECT i.email, 100.0*SUM(c.depot_id) OVER (PARTITION BY i.email, c.depot_id)/ COUNT(1) OVER () AS contrib
	FROM changesets c
	INNER JOIN identities i ON (c.author_id = i.id)
) t 
GROUP BY t.email
ORDER BY m DESC;


-- SELECT i.id, c.depot_id, 100*COUNT(c)/(
-- 	SELECT COUNT(1)
-- 	FROM changesets c2	
-- 	WHERE c2.depot_id = c.depot_id 
-- ) AS contrib 
-- FROM changesets c
-- INNER JOIN identities i ON (c.author_id = i.id)
-- GROUP BY depot_id, i.id
-- ORDER BY contrib DESC 


