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

package org.mozkito.libraries.logging.terminal;

import java.util.regex.Pattern;

/**
 * The Enum TerminalColor.
 */
public enum TerminalColor {

	/** The BLACK. */
	BLACK ("\u001b[30m"),

	/** The BACKGROUND_BLACK. */
	BGBLACK ("\u001b[40m"),

	/** The RED. */
	RED ("\u001b[31m"),

	/** The BACKGROUND_RED. */
	BGRED ("\u001b[41m"),

	/** The GREEN. */
	GREEN ("\u001b[32m"),

	/** The BACKGROUND_GREEN. */
	BGGREEN ("\u001b[42m"),

	/** The YELLOW. */
	YELLOW ("\u001b[33m"),

	/** The BACKGROUND_YELLOW. */
	BGYELLOW ("\u001b[43m"),

	/** The BLUE. */
	BLUE ("\u001b[34m"),

	/** The BACKGROUND_BLUE. */
	BGBLUE ("\u001b[44m"),

	/** The MAGENTA. */
	MAGENTA ("\u001b[35m"),

	/** The BACKGROUND_MAGENTA. */
	BGMAGENTA ("\u001b[45m"),

	/** The CYAN. */
	CYAN ("\u001b[36m"),

	/** The BACKGROUND_CYAN. */
	BGCYAN ("\u001b[46m"),

	/** The WHITE. */
	WHITE ("\u001b[37m"),

	/** The BACKGROUND_WHITE. */
	BGWHITE ("\u001b[47m"),

	/** The BOLD. */
	BOLD ("\u001b[1m"),

	/** The UNDERLINE. */
	UNDERLINE ("\u001b[4m"),

	/** The BLINK. */
	BLINK ("\u001b[5m"),

	/** The INVERT. */
	INVERT ("\u001b[7m"),

	/** The NONE. */
	NONE ("\u001b[m"),

	/** The BACKGROUND_NONE. */
	BGNONE ("\u001b[48m");

	/** The supported. */
	private static boolean SUPPORTED = false;

	static {
		if (System.getProperty("disableTermColors") == null) {
			if (System.console() != null) { // avoid colors when piping output
				final String termVariable = System.getenv("TERM");
				if (termVariable != null) {
					final Pattern pattern = Pattern.compile(".*color.*", Pattern.CASE_INSENSITIVE);
					TerminalColor.SUPPORTED = pattern.matcher(termVariable).matches()
					        || termVariable.equalsIgnoreCase("screen");
				}
			}
		}
	}
	
	/**
	 * Checks if is supported.
	 *
	 * @return true, if is supported
	 */
	public static boolean isSupported() {
		return TerminalColor.SUPPORTED;
	}

	/** The tag. */
	private final String tag;

	/**
	 * Instantiates a new terminal color.
	 *
	 * @param tag
	 *            the tag
	 */
	TerminalColor(final String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public String getTag() {
		return isSupported()
				? this.tag
				: "";
	}
}
