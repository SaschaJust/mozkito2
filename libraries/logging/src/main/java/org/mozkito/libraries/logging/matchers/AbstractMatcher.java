/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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
