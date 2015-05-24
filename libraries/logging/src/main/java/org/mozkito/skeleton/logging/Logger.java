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

import org.mozkito.skeleton.logging.consumer.LogConsumer;
import org.mozkito.skeleton.logging.consumer.appender.TerminalAppender;
import org.mozkito.skeleton.logging.internal.LogStream;

/**
 * The Class Logger.
 *
 * @author Sascha Just
 */
public class Logger {
	
	static {
		new LogConsumer(Bus.provider).register(new TerminalAppender(Level.DEBUG));
	}
	
	/** The Constant error. */
	public static final LogStream error        = new LogStream(Level.ERROR);
	
	/** The Constant fatal. */
	public static final LogStream fatal        = new LogStream(Level.FATAL);
	
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
	
	/** The Constant level. */
	private static final Level    level        = Level.INFO;
	
	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void debug(final String message) {
		Bus.notify(Level.DEBUG, Line.LOGS, String.valueOf(message));
	}
	
	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void debug(final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.DEBUG, Line.LOGS, null, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void debug(final Throwable throwable) {
		Bus.notify(Level.DEBUG, Line.LOGS, throwable, null);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void debug(final Throwable throwable,
	                         final String message) {
		Bus.notify(Level.DEBUG, Line.LOGS, throwable, message);
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
	public static void debug(final Throwable throwable,
	                         final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.DEBUG, Line.LOGS, throwable, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void error(final String message) {
		Bus.notify(Level.ERROR, Line.LOGS, String.valueOf(message));
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
	public static void fatal(final String message) {
		Bus.notify(Level.FATAL, Line.LOGS, String.valueOf(message));
	}
	
	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void fatal(final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.FATAL, Line.LOGS, null, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void fatal(final Throwable throwable) {
		Bus.notify(Level.FATAL, Line.LOGS, throwable, null);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void fatal(final Throwable throwable,
	                         final String message) {
		Bus.notify(Level.FATAL, Line.LOGS, throwable, message);
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
	public static void fatal(final Throwable throwable,
	                         final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.FATAL, Line.LOGS, throwable, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void info(final Object message) {
		Bus.notify(Level.INFO, Line.LOGS, String.valueOf(message));
	}
	
	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void info(final String formatString,
	                        final Object... arguments) {
		Bus.notify(Level.INFO, Line.LOGS, null, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void info(final Throwable throwable) {
		Bus.notify(Level.INFO, Line.LOGS, throwable, null);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void info(final Throwable throwable,
	                        final String message) {
		Bus.notify(Level.INFO, Line.LOGS, throwable, message);
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
	public static void info(final Throwable throwable,
	                        final String formatString,
	                        final Object... arguments) {
		Bus.notify(Level.INFO, Line.LOGS, throwable, formatString, arguments);
	}
	
	/**
	 * Log verbose.
	 * 
	 * @return true, if successful
	 */
	public static boolean logAlways() {
		return level.compareTo(Level.OFF) > 0;
	}
	
	/**
	 * Log debug.
	 * 
	 * @return true, if successful
	 */
	public static boolean logDebug() {
		return level.compareTo(Level.DEBUG) >= 0;
	}
	
	/**
	 * Log error.
	 * 
	 * @return true, if successful
	 */
	public static boolean logError() {
		return level.compareTo(Level.ERROR) >= 0;
	}
	
	/**
	 * Log info.
	 * 
	 * @return true, if successful
	 */
	public static boolean logInfo() {
		return level.compareTo(Level.INFO) >= 0;
	}
	
	/**
	 * Log trace.
	 * 
	 * @return true, if successful
	 */
	public static boolean logTrace() {
		return level.compareTo(Level.TRACE) >= 0;
	}
	
	/**
	 * Log error.
	 * 
	 * @return true, if successful
	 */
	public static boolean logWarn() {
		return level.compareTo(Level.WARN) >= 0;
	}
	
	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void trace(final String message) {
		Bus.notify(Level.TRACE, Line.LOGS, String.valueOf(message));
	}
	
	/**
	 * Error.
	 *
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void trace(final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.TRACE, Line.LOGS, null, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 */
	public static void trace(final Throwable throwable) {
		Bus.notify(Level.TRACE, Line.LOGS, throwable, null);
	}
	
	/**
	 * Error.
	 *
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 */
	public static void trace(final Throwable throwable,
	                         final String message) {
		Bus.notify(Level.TRACE, Line.LOGS, throwable, message);
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
	public static void trace(final Throwable throwable,
	                         final String formatString,
	                         final Object... arguments) {
		Bus.notify(Level.TRACE, Line.LOGS, throwable, formatString, arguments);
	}
	
	/**
	 * Error.
	 *
	 * @param message
	 *            the message
	 */
	public static void warn(final String message) {
		Bus.notify(Level.WARN, Line.LOGS, String.valueOf(message));
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
