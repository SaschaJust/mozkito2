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
import java.util.Arrays;

import org.mozkito.libraries.logging.matchers.IMatcher;
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
	
	/** The matcher. */
	private IMatcher        matcher;
	
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
	 * Matcher.
	 *
	 * @return the i matcher
	 */
	public IMatcher matcher() {
		return this.matcher;
	}
	
	/**
	 * Adds the matcher.
	 *
	 * @param matcher
	 *            the matcher
	 * @return true, if successful
	 */
	public boolean matcher(final IMatcher matcher) {
		if (matcher == null) {
			this.matcher = matcher;
			return true;
		} else {
			return false;
		}
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
