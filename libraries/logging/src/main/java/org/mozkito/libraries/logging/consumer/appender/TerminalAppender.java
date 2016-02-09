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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.LogEvent;
import org.mozkito.libraries.logging.matchers.IMatcher;
import org.mozkito.libraries.logging.terminal.ConsoleHighlighter;
import org.mozkito.libraries.logging.terminal.TerminalColor;
import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class TerminalAppender.
 *
 * @author Sascha Just
 */
public class TerminalAppender extends Appender {
	
	private static final TerminalColor[]            highlighterColors = new TerminalColor[] { TerminalColor.CYAN,
	        TerminalColor.MAGENTA, TerminalColor.BLUE, TerminalColor.GREEN, TerminalColor.YELLOW, TerminalColor.RED };
	
	private final Map<IMatcher, ConsoleHighlighter> highlighters      = new HashMap<>();
	
	private final PrintStream                       stream;
	
	/**
	 * Instantiates a new terminal appender.
	 *
	 * @param level
	 *            the level
	 */
	public TerminalAppender(final Level level) {
		super(level, DateTimeFormatter.ofPattern("dd/MM HH:mm"));
		this.stream = System.out;
	}
	
	/**
	 * Adds the highlighter.
	 *
	 * @param matcher
	 *            the highlighter
	 */
	public void addMatcher(final IMatcher matcher) {
		if (!this.highlighters.containsKey(matcher)) {
			this.highlighters.put(matcher, new ConsoleHighlighter() {
				
				/**
				 * {@inheritDoc}
				 * 
				 * @see org.mozkito.libraries.logging.terminal.ConsoleHighlighter#colors()
				 */
				@Override
				public TerminalColor[] colors() {
					final List<TerminalColor> myColoes = new LinkedList<TerminalColor>();
					myColoes.add(highlighterColors[TerminalAppender.this.highlighters.size() % highlighterColors.length]);
					if (TerminalAppender.this.highlighters.size() / highlighterColors.length % 2 == 1) {
						myColoes.add(TerminalColor.BOLD);
					}
					if (2 * TerminalAppender.this.highlighters.size() / highlighterColors.length % 2 == 1) {
						myColoes.add(TerminalColor.UNDERLINE);
					}
					if (4 * TerminalAppender.this.highlighters.size() / highlighterColors.length % 2 == 1) {
						myColoes.add(TerminalColor.INVERT);
					}
					if (8 * TerminalAppender.this.highlighters.size() / highlighterColors.length % 2 == 1) {
						myColoes.add(TerminalColor.BLINK);
					}
					return (TerminalColor[]) myColoes.toArray();
				}
			});
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
				                                  ? TerminalColor.BGRED.getTag() + TerminalColor.BLACK.getTag()
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
	 * @see org.mozkito.libraries.logging.consumer.appender.Appender#log(org.mozkito.libraries.logging.LogEvent)
	 */
	@Override
	public void log(final LogEvent event) {
		String line = String.format("%s%-5s%s [%s] [%s] %s%s",
		                            levelToColor(event.level()),
		                            event.level().name(),
		                            levelToColor(Level.OFF),
		                            this.dtFormatter.format(ZonedDateTime.ofInstant(event.timestamp(),
		                                                                            ZoneId.systemDefault())),
		                            event.threadName(), event.entryPoint() != null
		                                                                          ? event.entryPoint()
		                                                                          : "",
		                            event.arguments().length == 0
		                                                         ? event.message()
		                                                         : String.format(event.message(), event.arguments()));
		
		String additionalLines[] = new String[0];
		if (event.throwable() != null) {
			final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
			event.throwable().printStackTrace(new PrintStream(baoStream));
			additionalLines = baoStream.toString().split(System.lineSeparator());
		}
		
		synchronized (this.stream) {
			Asserts.notNull(event);
			Asserts.notNull(event.level());
			
			if (event.matcher() != null) {
				if (!this.highlighters.containsKey(event.matcher())) {
					addMatcher(event.matcher());
				}
				
				line = this.highlighters.get(event.matcher()).highlight(line);
			}
			
			this.stream.println(line);
			
			for (String additionalLine : additionalLines) {
				if (event.matcher() != null) {
					additionalLine = this.highlighters.get(event.matcher()).highlight(additionalLine);
				}
				
				additionalLine = String.format("%s%-5s%s [%s] [%s] %s%s",
				                               levelToColor(event.level()),
				                               event.level().name(),
				                               levelToColor(Level.OFF),
				                               this.dtFormatter.format(ZonedDateTime.ofInstant(event.timestamp(),
				                                                                               ZoneId.systemDefault())),
				                               event.threadName(), event.entryPoint() != null
				                                                                             ? event.entryPoint()
				                                                                             : "", additionalLine);
				
				this.stream.println(additionalLine);
			}
		}
		
	}
	
}
