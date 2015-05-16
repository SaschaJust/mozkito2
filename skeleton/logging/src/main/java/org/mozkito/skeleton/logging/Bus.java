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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.logging.consumer.LogConsumer;

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
		private final Set<IConsumer> fatal = new HashSet<IConsumer>();

		/** The error. */
		private final Set<IConsumer> error = new HashSet<IConsumer>();

		/** The warn. */
		private final Set<IConsumer> warn  = new HashSet<IConsumer>();

		/** The info. */
		private final Set<IConsumer> info  = new HashSet<IConsumer>();

		/** The debug. */
		private final Set<IConsumer> debug = new HashSet<IConsumer>();

		/** The trace. */
		private final Set<IConsumer> trace = new HashSet<IConsumer>();

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
		public void updateSubscription(final LogConsumer consumer) {
			synchronized (this) {
				this.trace.remove(consumer);
				this.debug.remove(consumer);
				this.info.remove(consumer);
				this.warn.remove(consumer);
				this.error.remove(consumer);
				this.fatal.remove(consumer);
				subscribe(consumer);
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
