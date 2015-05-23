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

package org.mozkito.skeleton.logging.consumer.appender;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.logging.Level;
import org.mozkito.skeleton.logging.LogEvent;
import org.mozkito.skeleton.logging.terminal.Highlighter;
import org.mozkito.skeleton.logging.terminal.TerminalColor;

/**
 * The Class TerminalAppender.
 *
 * @author Sascha Just
 */
public class TerminalAppender extends Appender {
	
	/** The highlighters. */
	private final Map<Level, Set<Highlighter>> highlighters = new HashMap<Level, Set<Highlighter>>();
	
	/**
	 * Instantiates a new terminal appender.
	 *
	 * @param level
	 *            the level
	 */
	public TerminalAppender(final Level level) {
		super(level, System.out, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	}
	
	/**
	 * Adds the highlighter.
	 *
	 * @param highlighter
	 *            the highlighter
	 */
	public void addHighlighter(final Highlighter highlighter) {
		final Level[] values = Level.values();
		
		for (int i = highlighter.getMin().ordinal(); i <= highlighter.getMax().ordinal(); ++i) {
			if (!this.highlighters.containsKey(values[i])) {
				this.highlighters.put(values[i], new HashSet<Highlighter>());
			}
			this.highlighters.get(values[i]).add(highlighter);
		}
	}
	
	/**
	 * Level to color.
	 *
	 * @param level
	 *            the level
	 * @return the string
	 */
	private String levelToColor(final Level level) {
		switch (level) {
			case FATAL:
				return TerminalColor.isSupported()
				                                  ? TerminalColor.BGRED + TerminalColor.BLACK.getTag()
				                                  : "";
			case ERROR:
				return TerminalColor.isSupported()
				                                  ? TerminalColor.RED.getTag()
				                                  : "";
			case WARN:
				return TerminalColor.isSupported()
				                                  ? TerminalColor.YELLOW.getTag()
				                                  : "";
			case OFF:
				return TerminalColor.isSupported()
				                                  ? TerminalColor.NONE.getTag()
				                                  : "";
			default:
				return "";
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.logging.consumer.appender.Appender#log(org.mozkito.skeleton.logging.LogEvent)
	 */
	@Override
	public void log(final LogEvent event) {
		String line = MessageFormat.format("{0}{1}{2} [{3}] [{4}] {5}{6}",
		                                   levelToColor(event.level()),
		                                   event.level().name(),
		                                   levelToColor(Level.OFF),
		                                   this.dtFormatter.format(ZonedDateTime.ofInstant(event.timestamp(),
		                                                                                   ZoneId.systemDefault())),
		                                   event.threadName(),
		                                   event.entryPoint() != null
		                                                             ? event.entryPoint()
		                                                             : "",
		                                   event.arguments().length == 0
		                                                                ? event.message()
		                                                                : String.format(event.message(),
		                                                                                event.arguments()));
		
		String additionalLines[] = new String[0];
		if (event.throwable() != null) {
			final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
			event.throwable().printStackTrace(new PrintStream(baoStream));
			additionalLines = baoStream.toString().split(System.lineSeparator());
		}
		
		synchronized (this.stream) {
			Asserts.notNull(event);
			Asserts.notNull(event.level());
			Asserts.notNull(this.highlighters);
			
			if (this.highlighters.containsKey(event.level())) {
				for (final Highlighter highlighter : this.highlighters.get(event.level())) {
					line = highlighter.highlight(line);
				}
			}
			this.stream.println(line);
			
			for (String additionalLine : additionalLines) {
				if (this.highlighters.containsKey(event.level())) {
					for (final Highlighter highlighter : this.highlighters.get(event.level())) {
						additionalLine = highlighter.highlight(additionalLine);
					}
				}
				additionalLine = MessageFormat.format("[{0}] [{1}] {2}{3}{4} {5}{6}",
				                                      this.dtFormatter.format(ZonedDateTime.ofInstant(event.timestamp(),
				                                                                                      ZoneId.systemDefault())),
				                                      event.threadName(), levelToColor(event.level()), event.level()
				                                                                                            .name(),
				                                      levelToColor(Level.OFF),
				                                      event.entryPoint() != null
				                                                                ? event.entryPoint()
				                                                                : "", additionalLine);
				this.stream.println(additionalLine);
			}
		}
		
	}
	
}
