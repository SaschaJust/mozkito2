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

package org.mozkito.skeleton.logging;

import org.mozkito.skeleton.logging.internal.LogStream;

/**
 * The Class Logger.
 *
 * @author Sascha Just
 */
public class Logger {

	/** The Constant error. */
	public static final LogStream error        = new LogStream(Level.ERROR);

	/** The Constant warn. */
	public static final LogStream warn         = new LogStream(Level.WARN);

	/** The Constant info. */
	public static final LogStream info         = new LogStream(Level.INFO);

	/** The Constant debug. */
	public static final LogStream debug        = new LogStream(Level.DEBUG);

	/** The Constant trace. */
	public static final LogStream trace        = new LogStream(Level.TRACE);

	/** The Constant notification. */
	public static final LogStream notification = new LogStream(Level.INFO, Line.NOTIFICATIONS);

	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void error(final String message) {
		Bus.notify(Level.ERROR, Line.LOGS, message);
	}

	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void error(final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.ERROR, Line.LOGS, null, formatString, arguments);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void error(final Throwable throwable) {
		Bus.notify(Level.ERROR, Line.LOGS, throwable, null);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void error(final Throwable throwable,
	                         final String message) {
		Bus.notify(Level.ERROR, Line.LOGS, throwable, message);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void error(final Throwable throwable,
	                         final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.ERROR, Line.LOGS, throwable, formatString, arguments);
	}

	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void warn(final String message) {
		Bus.notify(Level.WARN, Line.LOGS, message);
	}

	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void warn(final String formatString,
	                        final Object... arguments) {
		Bus.notify(Level.WARN, Line.LOGS, null, formatString, arguments);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void warn(final Throwable throwable) {
		Bus.notify(Level.WARN, Line.LOGS, throwable, null);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void warn(final Throwable throwable,
	                        final String message) {
		Bus.notify(Level.WARN, Line.LOGS, throwable, message);
	}

	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void warn(final Throwable throwable,
	                        final String formatString,
	                        final Object... arguments) {
		Bus.notify(Level.WARN, Line.LOGS, throwable, formatString, arguments);
	}

	/**
	 * Instantiates a new logger.
	 */
	private Logger() {
		// avoid instantiation
	}
}
