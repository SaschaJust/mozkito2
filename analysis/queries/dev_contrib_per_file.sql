-- per day active
SELECT i.email, MAX(churn_per_day) cpd FROM (
	SELECT c.author_id, c.authored_time::date, AVG(SUM(lines_in)+SUM(lines_out)) OVER (PARTITION BY c.author_id) AS churn_per_day
	FROM changesets c 
	INNER JOIN revisions r ON (r.changeset_id = c.id AND c.depot_id = r.depot_id)
	GROUP BY c.author_id, c.authored_time::date
) t
INNER JOIN identities i ON (author_id = i.id) 
GROUP BY i.email
ORDER BY cpd DESC;

-- per days total 
SELECT i.email, MAX(churn_per_day) cpd FROM (
	SELECT c.author_id, (SUM(lines_in) OVER w1 + SUM(lines_out) OVER w1)::numeric / (MAX(c.authored_time::date) OVER () - MIN(c.authored_time::date) OVER ()) AS churn_per_day
	FROM changesets c 
	INNER JOIN revisions r ON (r.changeset_id = c.id AND c.depot_id = r.depot_id)
	WINDOW w1 AS (PARTITION BY c.author_id)
) t
INNER JOIN identities i ON (author_id = i.id) 
GROUP BY i.email
ORDER BY cpd DESC;
