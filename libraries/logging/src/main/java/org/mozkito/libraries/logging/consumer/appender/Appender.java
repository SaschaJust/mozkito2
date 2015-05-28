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
