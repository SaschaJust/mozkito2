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

package org.mozkito.libraries.logging.consumer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mozkito.libraries.logging.Bus.LogProvider;
import org.mozkito.libraries.logging.IConsumer;
import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.LogEvent;
import org.mozkito.libraries.logging.consumer.appender.Appender;

/**
 * The Class LogConsumer.
 *
 * @author Sascha Just
 */
public class LogConsumer implements IConsumer {
	
	/** The appenders. */
	private final Map<Level, Set<Appender>> appenders = new HashMap<Level, Set<Appender>>();
	
	/** The provider. */
	private final LogProvider               provider;
	
	/**
	 * Instantiates a new log consumer.
	 *
	 * @param provider
	 *            the provider
	 */
	public LogConsumer(final LogProvider provider) {
		this.provider = provider;
		provider.subscribe(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.logging.IConsumer#consume(org.mozkito.libraries.logging.LogEvent)
	 */
	@Override
	public void consume(final LogEvent event) {
		for (final Appender appender : responsible(event.level())) {
			appender.log(event);
		}
	}
	
	/**
	 * Max level.
	 *
	 * @return the level
	 */
	public Level maxLevel() {
		Level level = Level.OFF;
		for (final Level l : this.appenders.keySet()) {
			if (level.compareTo(l) < 0) {
				level = l;
			}
		}
		
		return level;
	}
	
	/**
	 * Register.
	 *
	 * @param appender
	 *            the appender
	 * @return true, if successful
	 */
	public boolean register(final Appender appender) {
		this.provider.unsubscribe(this);
		
		if (!this.appenders.containsKey(appender.getLevel())) {
			this.appenders.put(appender.getLevel(), new HashSet<Appender>());
		}
		final boolean success = this.appenders.get(appender.getLevel()).add(appender);
		
		this.provider.subscribe(this);
		
		return success;
	}
	
	/**
	 * Responsible.
	 *
	 * @param level
	 *            the level
	 * @return the sets the
	 */
	private Set<Appender> responsible(final Level level) {
		final Set<Appender> responsibles = new HashSet<Appender>();
		Set<Appender> temp = null;
		for (final Level l : Level.values()) {
			if (l.compareTo(level) >= 0) {
				temp = this.appenders.get(l);
				if (temp != null) {
					responsibles.addAll(temp);
				}
			}
		}
		
		return responsibles;
	}
	
	/**
	 * Unregister.
	 *
	 * @param appender
	 *            the appender
	 */
	public void unregister(final Appender appender) {
		this.provider.unsubscribe(this);
		
		for (final Map.Entry<Level, Set<Appender>> entry : this.appenders.entrySet()) {
			entry.getValue().remove(appender);
		}
		
		this.provider.subscribe(this);
	}
	
}
