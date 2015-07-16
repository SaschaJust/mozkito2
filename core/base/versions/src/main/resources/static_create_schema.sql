CREATE TABLE static_integration_types (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(5) UNIQUE NOT NULL
);

CREATE INDEX static_integration_types_name ON static_integration_types (name);

CREATE TABLE static_change_types (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(14) UNIQUE NOT NULL	
);

CREATE INDEX static_change_types_name ON static_change_types (name);

CREATE TABLE static_reference_types (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(6) UNIQUE NOT NULL	
);

CREATE INDEX static_reference_types_name ON static_reference_types (name);

CREATE TABLE static_branch_markers (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(13) UNIQUE NOT NULL	
);

CREATE INDEX static_branch_markers_name ON static_branch_markers (name);

CREATE TABLE static_integration_markers (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(9) UNIQUE NOT NULL	
);

CREATE INDEX static_integration_markers_name ON static_integration_markers (name);

CREATE TABLE static_navigation_markers (
	value SMALLINT PRIMARY KEY,
	name VARCHAR(7) UNIQUE NOT NULL	
);

CREATE INDEX static_navigation_markers_name ON static_navigation_markers (name);
