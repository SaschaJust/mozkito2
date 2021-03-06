/***********************************************************************************************************************
 * Copyright 2015 mozkito.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 **********************************************************************************************************************/
package org.mozkito.core.libs.versions.repositories;

import java.time.Instant;

import org.mozkito.libraries.logging.Logger;

/**
 * The Class AnnotationEntry.
 */
public class AnnotationEntry {
	
	/** The alternative file path. */
	private final String  alternativeFilePath;
	
	/** The line. */
	private final String  line;
	
	/** The revision. */
	private final String  revision;
	
	/** The timestamp. */
	private final Instant timestamp;
	
	/** The username. */
	private final String  username;
	
	/**
	 * Instantiates a new annotation entry.
	 * 
	 * @param revision
	 *            the revision
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @param line
	 *            the line
	 */
	public AnnotationEntry(final String revision, final String username, final Instant timestamp, final String line) {
		this(revision, username, timestamp, line, null);
	}
	
	/**
	 * Instantiates a new annotation entry.
	 * 
	 * @param revision
	 *            the revision
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @param line
	 *            the line
	 * @param alternativeFilePath
	 *            the alternative file path
	 */
	public AnnotationEntry(final String revision, final String username, final Instant timestamp, final String line,
	        final String alternativeFilePath) {
		this.revision = revision;
		this.alternativeFilePath = alternativeFilePath;
		this.username = username;
		this.timestamp = timestamp;
		this.line = line;
		
		if (Logger.logTrace()) {
			Logger.trace("Creating " + getClassName() + ": " + this);
		}
	}
	
	/**
	 * Gets the alternative file path.
	 * 
	 * @return the alternative file path
	 */
	public String getAlternativeFilePath() {
		return this.alternativeFilePath;
		
	}
	
	/**
	 * Gets the handle.
	 * 
	 * @return the handle
	 */
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Gets the line.
	 * 
	 * @return the line
	 */
	public String getLine() {
		return this.line;
	}
	
	/**
	 * Gets the revision.
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return this.revision;
	}
	
	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public Instant getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Checks for alternative path.
	 * 
	 * @return true, if successful
	 */
	public boolean hasAlternativePath() {
		return this.alternativeFilePath != null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnnotationEntry [revision=" + this.revision + ", alternativeFilePath=" + this.alternativeFilePath
		        + ", username=" + this.username + ", timestamp=" + this.timestamp + ", line=" + this.line + "]";
	}
	
}
