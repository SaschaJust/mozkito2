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

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mozkito.libraries.logging.consumer.LogConsumer;
import org.mozkito.libraries.logging.matchers.IMatcher;
import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class Bus.
 *
 * @author Sascha Just
 */
public class Bus {
	
	/**
	 * The Class Caller.
	 */
	public static class Caller {
		
		/** The class name. */
		public final String className;
		
		/** The method name. */
		public final String methodName;
		
		/** The line number. */
		public final int    lineNumber;
		
		/**
		 * Instantiates a new caller.
		 *
		 * @param className
		 *            the class name
		 * @param methodName
		 *            the method name
		 * @param lineNumber
		 *            the line number
		 */
		public Caller(final String className, final String methodName, final int lineNumber) {
			this.className = className;
			this.methodName = methodName;
			this.lineNumber = lineNumber;
		}
		
		/**
		 * {@inheritDoc}
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.className + "::" + this.methodName + "#" + this.lineNumber;
		}
	}
	
	/**
	 * The Class LogProvider.
	 */
	public static class LogProvider {
		
		/** The fatal. */
		private final Set<IConsumer> fatal    = new HashSet<IConsumer>();
		
		/** The error. */
		private final Set<IConsumer> error    = new HashSet<IConsumer>();
		
		/** The warn. */
		private final Set<IConsumer> warn     = new HashSet<IConsumer>();
		
		/** The info. */
		private final Set<IConsumer> info     = new HashSet<IConsumer>();
		
		/** The debug. */
		private final Set<IConsumer> debug    = new HashSet<IConsumer>();
		
		/** The trace. */
		private final Set<IConsumer> trace    = new HashSet<IConsumer>();
		
		/** The matchers. */
		private final List<IMatcher> matchers = new LinkedList<>();
		
		/**
		 * Highlight.
		 *
		 * @param matcher
		 *            the matcher
		 */
		public void highlight(final IMatcher matcher) {
			this.matchers.add(matcher);
		}
		
		/**
		 * Notify.
		 *
		 * @param logEvent
		 *            the log event
		 */
		public void notify(final LogEvent logEvent) {
			Set<IConsumer> affected = new HashSet<IConsumer>();
			switch (logEvent.level()) {
				case FATAL:
					affected = this.fatal;
					break;
				case ERROR:
					affected = this.error;
					break;
				case WARN:
					affected = this.warn;
					break;
				case INFO:
					affected = this.info;
					break;
				case DEBUG:
					affected = this.debug;
					break;
				case TRACE:
					affected = this.trace;
					break;
				default:
					// ignore anything else
					break;
			}
			
			if (!affected.isEmpty()) {
				for (final IMatcher matcher : this.matchers) {
					if (matcher.matches(logEvent.message(), logEvent.level(), logEvent.threadName())) {
						logEvent.matcher(matcher);
						break;
					}
				}
			}
			
			for (final IConsumer l : affected) {
				l.consume(logEvent);
			}
		}
		
		/**
		 * Subscribe.
		 *
		 * @param consumer
		 *            the consumer
		 */
		public void subscribe(final LogConsumer consumer) {
			synchronized (this) {
				switch (consumer.maxLevel()) {
					case TRACE:
						this.trace.add(consumer); //$FALL-THROUGH$
					case DEBUG:
						this.debug.add(consumer); //$FALL-THROUGH$
					case INFO:
						this.info.add(consumer); //$FALL-THROUGH$
					case WARN:
						this.warn.add(consumer); //$FALL-THROUGH$
					case ERROR:
						this.error.add(consumer); //$FALL-THROUGH$
					case FATAL:
						this.fatal.add(consumer);
						break;
					default:
						break;
				}
			}
		}
		
		/**
		 * Update subscription.
		 *
		 * @param consumer
		 *            the consumer
		 */
		public void unsubscribe(final LogConsumer consumer) {
			synchronized (this) {
				this.trace.remove(consumer);
				this.debug.remove(consumer);
				this.info.remove(consumer);
				this.warn.remove(consumer);
				this.error.remove(consumer);
				this.fatal.remove(consumer);
			}
		}
	}
	
	/** The Constant DEBUG_ENABLED. */
	public static final boolean     DEBUG_ENABLED   = System.getProperty("debug") != null;
	
	/** The Constant LOGGING_PACKAGE. */
	private static final String     LOGGING_PACKAGE = Logger.class.getPackage().getName();
	
	/** The provider. */
	public static final LogProvider provider        = new LogProvider();
	
	/**
	 * Entry point.
	 *
	 * @return the caller
	 */
	public static Caller entryPoint() {
		final Throwable throwable = new Throwable();
		throwable.fillInStackTrace();
		
		final StackTraceElement[] stackTrace = throwable.getStackTrace();
		int offset = 0;
		OFFSET_SEARCH: for (offset = 0; offset < stackTrace.length; ++offset) {
			if (!stackTrace[offset].getClassName().startsWith(Bus.LOGGING_PACKAGE)) {
				break OFFSET_SEARCH;
			}
		}
		
		Asserts.greater(stackTrace.length,
		                offset,
		                "The length of the created stacktrace must never be less than the specified offset (which determines the original location).");
		
		final Caller caller = new Caller(throwable.getStackTrace()[offset].getClassName(),
		                                 throwable.getStackTrace()[offset].getMethodName(),
		                                 throwable.getStackTrace()[offset].getLineNumber());
		
		return caller;
	}
	
	/**
	 * Gets the log provider.
	 *
	 * @return the log provider
	 */
	public static LogProvider getLogProvider() {
		return provider;
	}
	
	/**
	 * Notify.
	 *
	 * @param level
	 *            the level
	 * @param line
	 *            the line
	 * @param message
	 *            the message
	 */
	public static void notify(final Level level,
	                          final Line line,
	                          final String message) {
		notify(level, line, null, message);
	}
	
	/**
	 * Notify.
	 *
	 * @param level
	 *            the level
	 * @param line
	 *            the line
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 * @param args
	 *            the args
	 */
	public static void notify(final Level level,
	                          final Line line,
	                          final Throwable throwable,
	                          final String message,
	                          final Object... args) {
		final Instant timestamp = java.time.Instant.now();
		// TODO complete this
		switch (line) {
			case LOGS:
				Bus.provider.notify(new LogEvent(timestamp, level, throwable, message, args));
				break;
			case NOTIFICATIONS:
				Bus.provider.notify(new NotificationEvent(timestamp, level, throwable, message, args));
				break;
			default:
				break;
		}
	}
	
	/**
	 * Instantiates a new bus.
	 */
	public Bus() {
	}
}
