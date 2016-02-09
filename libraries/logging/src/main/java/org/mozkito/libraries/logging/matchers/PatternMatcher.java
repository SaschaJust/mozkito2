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

/**
 * The Class PatternMatcher.
 *
 * @author Sascha Just
 */
public abstract class PatternMatcher extends AbstractMatcher {
	
	/** The pattern. */
	private final String pattern;
	
	/**
	 * Instantiates a new pattern matcher.
	 *
	 * @param pattern
	 *            the pattern
	 */
	public PatternMatcher(final String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.logging.matchers.AbstractMatcher#matches(java.lang.String,
	 *      org.mozkito.libraries.logging.Level, java.lang.String)
	 */
	@Override
	public final boolean matches(final String message,
	                             final Level level,
	                             final String threadName) {
		return message.matches(this.pattern);
	}
	
}
