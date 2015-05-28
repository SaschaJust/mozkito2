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

/**
 * The Class ContainsMatcher.
 *
 * @author Sascha Just
 */
public class ContainsMatcher extends AbstractMatcher {
	
	/** The substring. */
	private final String substring;
	
	/**
	 * Instantiates a new contains matcher.
	 *
	 * @param substring
	 *            the substring
	 */
	public ContainsMatcher(final String substring) {
		this.substring = substring;
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
		return message.contains(this.substring);
	}
	
}
