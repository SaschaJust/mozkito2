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
import java.util.Arrays;

import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class LogEvent.
 *
 * @author Sascha Just
 */
public class LogEvent {
	
	/** The thread name. */
	private final String    threadName;
	
	/** The entry point. */
	private String          entryPoint;
	
	/** The timestamp. */
	private final Instant   timestamp;
	
	/** The level. */
	private final Level     level;
	
	/** The message. */
	private final String    message;
	
	/** The throwable. */
	private final Throwable throwable;
	
	/** The arguments. */
	private final Object[]  arguments;
	
	/**
	 * Instantiates a new log event.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @param level
	 *            the level
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 * @param args
	 *            the args
	 */
	public LogEvent(final Instant timestamp, final Level level, final Throwable throwable, final String message,
	        final Object... args) {
		Requires.notNull(timestamp);
		Requires.notNull(level);
		
		this.timestamp = timestamp;
		this.level = level;
		this.message = message;
		this.throwable = throwable;
		this.arguments = args;
		
		if (Bus.DEBUG_ENABLED) {
			this.entryPoint = Bus.entryPoint().toString() + ' ';
		} else {
			this.entryPoint = null;
		}
		
		this.threadName = Thread.currentThread().getName();
	}
	
	/**
	 * Arguments.
	 *
	 * @return the arguments
	 */
	public final Object[] arguments() {
		return this.arguments;
	}
	
	/**
	 * Entry point.
	 *
	 * @return the entryPoint
	 */
	public final String entryPoint() {
		return this.entryPoint;
	}
	
	/**
	 * Level.
	 *
	 * @return the level
	 */
	public final Level level() {
		return this.level;
	}
	
	/**
	 * Message.
	 *
	 * @return the message
	 */
	public final String message() {
		return this.message;
	}
	
	/**
	 * Thread name.
	 *
	 * @return the threadName
	 */
	public final String threadName() {
		return this.threadName;
	}
	
	/**
	 * Throwable.
	 *
	 * @return the throwable
	 */
	public final Throwable throwable() {
		return this.throwable;
	}
	
	/**
	 * Timestamp.
	 *
	 * @return the timestamp
	 */
	public final Instant timestamp() {
		return this.timestamp;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LogEvent [threadName=");
		builder.append(this.threadName);
		if (this.entryPoint != null) {
			builder.append(". entryPoint=");
			builder.append(this.entryPoint);
		}
		builder.append(", timestamp=");
		builder.append(this.timestamp);
		builder.append(", level=");
		builder.append(this.level);
		builder.append(", message=");
		builder.append(this.message);
		builder.append(", throwable=");
		builder.append(this.throwable);
		builder.append(", arguments=");
		builder.append(Arrays.toString(this.arguments));
		builder.append("]");
		return builder.toString();
	}
	
}
