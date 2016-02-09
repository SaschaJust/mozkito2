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

package org.mozkito.libraries.logging;

import java.io.IOException;

import org.mozkito.libraries.logging.consumer.LogConsumer;
import org.mozkito.libraries.logging.consumer.appender.FileAppender;
import org.mozkito.libraries.logging.consumer.appender.TerminalAppender;
import org.mozkito.libraries.logging.internal.LogStream;

/**
 * The Class Logger.
 *
 * @author Sascha Just
 */
public class Logger {
	
	/** The log consumer. */
	private static LogConsumer      logConsumer;
	
	/** The terminal appender. */
	private static TerminalAppender terminalAppender;
	
	/** The file appender. */
	private static FileAppender     fileAppender;
	
	static {
		logConsumer = new LogConsumer(Bus.provider);
		
		terminalAppender = new TerminalAppender(Level.DEBUG);
		logConsumer.register(terminalAppender);
		
		try {
			fileAppender = new FileAppender(Level.DEBUG);
			logConsumer.register(fileAppender);
		} catch (final IOException e) {
			if (Logger.logError()) {
				Logger.error("Could not register log file appender.");
			}
		}
	}
	
	/** The Constant error. */
	public static final LogStream   error        = new LogStream(Level.ERROR);
	
	/** The Constant fatal. */
	public static final LogStream   fatal        = new LogStream(Level.FATAL);
	
	/** The Constant warn. */
	public static final LogStream   warn         = new LogStream(Level.WARN);
	
	/** The Constant info. */
	public static final LogStream   info         = new LogStream(Level.INFO);
	
	/** The Constant debug. */
	public static final LogStream   debug        = new LogStream(Level.DEBUG);
	
	/** The Constant trace. */
	public static final LogStream   trace        = new LogStream(Level.TRACE);
	
	/** The Constant notification. */
	public static final LogStream   notification = new LogStream(Level.INFO, Line.NOTIFICATIONS);
	
	/** The Constant level. */
	private static Level            level        = Level.OFF;
	
	/**
	 * Console level.
	 *
	 * @param consoleLevel
	 *            the file level
	 */
	public static void consoleLevel(final Level consoleLevel) {
		if (!terminalAppender.getLevel().equals(consoleLevel)) {
			if (consoleLevel.compareTo(level) > 0) {
				level = consoleLevel;
			}
			logConsumer.unregister(terminalAppender);
			terminalAppender.setLevel(consoleLevel);
			logConsumer.register(terminalAppender);
		}
	}
	
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
	 * File level.
	 *
	 * @param fileLevel
	 *            the file level
	 */
	public static void fileLevel(final Level fileLevel) {
		if (!fileAppender.getLevel().equals(fileLevel)) {
			if (fileLevel.compareTo(level) > 0) {
				level = fileLevel;
			}
			logConsumer.unregister(fileAppender);
			fileAppender.setLevel(fileLevel);
			logConsumer.register(fileAppender);
		}
	}
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public static Level getLevel() {
		return level;
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
