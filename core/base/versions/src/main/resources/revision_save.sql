INSERT INTO revisions (
		id,
		changeset_id,
		change_type,
		source_id,
		target_id,
		confidence,
		old_mode,
		new_mode,
		old_hash,
		new_hash,
		lines_in,
		lines_out
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)