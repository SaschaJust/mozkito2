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

package org.mozkito.libraries.logging.matchers;

import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.Logger;

/**
 * The Class AbstractMatcher.
 *
 * @author Sascha Just
 */
public abstract class AbstractMatcher implements IMatcher {
	
	/** The max level. */
	private Level maxLevel;
	
	/** The min level. */
	private Level minLevel;
	
	/**
	 * Instantiates a new abstract matcher.
	 */
	public AbstractMatcher() {
		this(Logger.getLevel());
	}
	
	/**
	 * Instantiates a new abstract matcher.
	 *
	 * @param maxLevel
	 *            the max level
	 */
	public AbstractMatcher(final Level maxLevel) {
		this(Level.FATAL, maxLevel);
	}
	
	/**
	 * Instantiates a new abstract matcher.
	 *
	 * @param minLevel
	 *            the min level
	 * @param maxLevel
	 *            the max level
	 */
	public AbstractMatcher(final Level minLevel, final Level maxLevel) {
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}
	
	/**
	 * Gets the max level.
	 *
	 * @return the maxLevel
	 */
	public final Level getMax() {
		return this.maxLevel;
	}
	
	/**
	 * Gets the min level.
	 *
	 * @return the minLevel
	 */
	public final Level getMin() {
		return this.minLevel;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.logging.matchers.IMatcher#matches(java.lang.String,
	 *      org.mozkito.libraries.logging.Level, java.lang.String)
	 */
	public abstract boolean matches(String message,
	                                Level level,
	                                String threadName);
	
	/**
	 * Sets the max level.
	 *
	 * @param maxLevel
	 *            the maxLevel to set
	 */
	public final void setMaxLevel(final Level maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	/**
	 * Sets the min level.
	 *
	 * @param minLevel
	 *            the minLevel to set
	 */
	public final void setMinLevel(final Level minLevel) {
		this.minLevel = minLevel;
	}
	
}
