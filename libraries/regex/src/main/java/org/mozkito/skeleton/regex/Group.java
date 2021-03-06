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
package org.mozkito.skeleton.regex;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Group.
 * 
 * @author Sascha Just 
 */
public class Group {
	
	/** The index. */
	private final int    index;
	
	/** The match. */
	private final String match;
	
	/** The name. */
	private final String name;
	
	/** The pattern. */
	private final String pattern;
	
	/** The start. */
	private final int    start;
	
	/** The end. */
	private final int    end;
	
	/** The text. */
	private final String text;
	
	/**
	 * Instantiates a new group.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param text
	 *            the text
	 * @param match
	 *            the match
	 * @param index
	 *            the index
	 * @param name
	 *            the name
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public Group(final String pattern, final String text, final String match, final int index, final String name,
	        final int start, final int end) {
		Requires.notNull(pattern);
		Requires.notNull(text);
		Requires.notNull(match);
		Requires.notNegative(index);
		Requires.notNegative(start);
		Requires.notNegative(end);
		
		this.pattern = pattern;
		this.text = text;
		this.match = match;
		this.index = index;
		this.name = name;
		this.end = end;
		this.start = start;
		
		Asserts.notNull(this.pattern, "Field '%s' in '%s'.", "pattern", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNull(this.text, "Field '%s' in '%s'.", "text", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNegative(this.index, "Field '%s' in '%s'.", "index", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNegative(this.start, "Field '%s' in '%s'.", "start", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNegative(this.end, "Field '%s' in '%s'.", "end", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Determines the end of the match (string index). Guaranteed to be non-negative.
	 * 
	 * @return the character index that determines the end of the match.
	 */
	public int end() {
		Asserts.notNegative(this.end, "Field '%s' in '%s'.", "end", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.end;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Group)) {
			return false;
		}
		final Group other = (Group) obj;
		if (this.index != other.index) {
			return false;
		}
		if (this.pattern == null) {
			if (other.pattern != null) {
				return false;
			}
		} else if (!this.pattern.equals(other.pattern)) {
			return false;
		}
		if (this.text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!this.text.equals(other.text)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the simple class name.
	 * 
	 * @return the simple class name
	 */
	public final String getHandle() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Gets the match.
	 * 
	 * @return the match
	 */
	public String getMatch() {
		return this.match;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the pattern.
	 * 
	 * @return the pattern
	 */
	public String getPattern() {
		return this.pattern;
	}
	
	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.index;
		result = prime * result + (this.pattern == null
		                                               ? 0
		                                               : this.pattern.hashCode());
		result = prime * result + (this.text == null
		                                            ? 0
		                                            : this.text.hashCode());
		return result;
	}
	
	/**
	 * Start.
	 * 
	 * @return the int
	 */
	public int start() {
		Asserts.notNegative(this.start, "Field '%s' in '%s'.", "start", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.start;
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
		final StringBuilder builder = new StringBuilder();
		builder.append("Group [index=");
		builder.append(this.index);
		builder.append(", match=");
		builder.append(this.match);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", start=");
		builder.append(this.start);
		builder.append(", end=");
		builder.append(this.end);
		builder.append("]");
		return builder.toString();
	}
	
}
