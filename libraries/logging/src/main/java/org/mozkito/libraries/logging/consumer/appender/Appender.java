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

package org.mozkito.libraries.logging.consumer.appender;

import java.time.format.DateTimeFormatter;

import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.LogEvent;

/**
 * The Class Appender.
 */
public abstract class Appender {
	
	/** The dt formatter. */
	protected final DateTimeFormatter dtFormatter;
	
	/** The level. */
	private Level                     level;
	
	/**
	 * Instantiates a new appender.
	 *
	 * @param level
	 *            the level
	 * @param formatter
	 *            the formatter
	 */
	public Appender(final Level level, final DateTimeFormatter formatter) {
		this.level = level;
		this.dtFormatter = formatter;
	}
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public Level getLevel() {
		return this.level;
	}
	
	/**
	 * Log.
	 *
	 * @param event
	 *            the event
	 */
	public abstract void log(LogEvent event);
	
	/**
	 * Sets the level.
	 *
	 * @param level
	 *            the new level
	 */
	public void setLevel(final Level level) {
		this.level = level;
	}
}
