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

import org.apache.commons.lang3.StringEscapeUtils;

import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.libraries.logging.Logger;

/**
 * The Class LogEntry.
 * 
 * @author Kim Herzig 
 */
public class LogEntry implements Comparable<LogEntry> {
	
	/** The author. */
	protected Identity author;
	
	/** The commit date. */
	protected Instant  commitDate;
	
	/** The message. */
	protected String   message;
	
	/** The previous. */
	protected LogEntry previous;
	
	/** The revision. */
	protected String   revision;
	
	/** The original id. */
	protected String   originalId;
	
	/**
	 * Instantiates a new log entry.
	 * 
	 * @param revision
	 *            the revision
	 * @param previous
	 *            the previous LogEntry, null if this is the first revision
	 * @param author
	 *            the author
	 * @param message
	 *            the message
	 * @param Instant
	 *            the date time
	 * @param originalId
	 *            the original id
	 */
	public LogEntry(final String revision, final LogEntry previous, final Identity author, final String message,
	        final Instant Instant, final String originalId) {
		this.revision = revision;
		this.author = author;
		this.message = message;
		if (this.message.endsWith(System.lineSeparator())) {
			this.message = this.message.substring(0, this.message.length() - 1);
		}
		
		this.previous = previous;
		this.commitDate = Instant;
		this.originalId = originalId;
		
		if (Logger.logTrace()) {
			Logger.trace("Creating " + getClassName() + ": " + this);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final LogEntry o) {
		return this.commitDate.compareTo(o.commitDate);
	}
	
	/**
	 * Gets the author.
	 * 
	 * @return the author
	 */
	public Identity getAuthor() {
		return this.author;
	}
	
	/**
	 * Gets the handle.
	 * 
	 * @return the simple class name of this class.
	 */
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Gets the date time.
	 * 
	 * @return the date time
	 */
	public Instant getInstant() {
		return this.commitDate;
	}
	
	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Returns the ID of the log entry in the original repository if the repository was converted (e.g. from SVN to Git)
	 * 
	 * @return the original id
	 */
	public String getOriginalId() {
		if (this.originalId != null) {
			return this.originalId;
		}
		return "";
	}
	
	/**
	 * Gets the revision.
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return this.revision;
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
		return "LogEntry [revision=" + this.revision + ", author=" + this.author + ", message="
		        + StringEscapeUtils.escapeJava(this.message) + ", commitDate=" + this.commitDate + ", previous="
		        + (this.previous != null
		                                ? this.previous.revision
		                                : "(null)") + "]";
	}
	
}
