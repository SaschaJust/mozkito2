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

package org.mozkito.skeleton.logging.consumer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mozkito.skeleton.logging.Bus.LogProvider;
import org.mozkito.skeleton.logging.IConsumer;
import org.mozkito.skeleton.logging.Level;
import org.mozkito.skeleton.logging.LogEvent;
import org.mozkito.skeleton.logging.consumer.appender.Appender;

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
	 * @see org.mozkito.skeleton.logging.IConsumer#consume(org.mozkito.skeleton.logging.LogEvent)
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
		if (!this.appenders.containsKey(appender.getLevel())) {
			this.appenders.put(appender.getLevel(), new HashSet<Appender>());
		}
		final boolean success = this.appenders.get(appender.getLevel()).add(appender);

		if (success) {
			this.provider.updateSubscription(this);
		}

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

}
