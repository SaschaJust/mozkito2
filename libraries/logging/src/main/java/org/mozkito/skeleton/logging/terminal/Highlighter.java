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
package org.mozkito.skeleton.logging.terminal;

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.logging.Level;

/**
 * The Class Highlighter.
 *
 * @author Sascha Just
 */
public abstract class Highlighter {

	/** The min. */
	private final Level min;

	/** The max. */
	private final Level max;

	/**
	 * Instantiates a new highlighter.
	 *
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public Highlighter(final Level min, final Level max) {
		Requires.notNull(min);
		Requires.notNull(max);

		this.min = min;
		this.max = max;
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public final Level getMax() {
		return this.max;
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public final Level getMin() {
		return this.min;
	}

	/**
	 * Highlight.
	 *
	 * @param message
	 *            the message
	 * @return the string
	 */
	public String highlight(final String message) {
		return String.format("%s%s%s%s", TerminalColor.MAGENTA.getTag(), TerminalColor.UNDERLINE.getTag(), message,
		                     TerminalColor.NONE.getTag());
	}

	/**
	 * Matches.
	 *
	 * @param message
	 *            the message
	 * @param level
	 *            the level
	 * @param prefix
	 *            the prefix
	 * @return true, if successful
	 */
	public abstract boolean matches(String message,
	                                Level level,
	                                String prefix);

}
