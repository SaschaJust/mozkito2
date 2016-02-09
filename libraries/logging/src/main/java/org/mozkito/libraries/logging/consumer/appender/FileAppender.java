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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.LogEvent;
import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class FileAppender.
 *
 * @author Sascha Just
 */
public class FileAppender extends Appender {
	
	/** The stream. */
	private final PrintStream stream;
	
	/**
	 * Instantiates a new file appender.
	 *
	 * @param level
	 *            the level
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FileAppender(final Level level) throws IOException {
		this(level, new File("mozkito.log"));
	}
	
	/**
	 * Instantiates a new file appender.
	 *
	 * @param level
	 *            the level
	 * @param logFile
	 *            the log file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FileAppender(final Level level, final File logFile) throws IOException {
		super(level, DateTimeFormatter.ofPattern("yyyy/dd/MM HH:mm:ss"));
		
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		this.stream = new PrintStream(new FileOutputStream(logFile));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.stream.flush();
		this.stream.close();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.logging.consumer.appender.Appender#log(org.mozkito.libraries.logging.LogEvent)
	 */
	@Override
	public void log(final LogEvent event) {
		final String line = String.format("%-5s [%s] [%s] %s%s",
		                                  event.level().name(),
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
			
			this.stream.println(line);
			
			for (String additionalLine : additionalLines) {
				
				additionalLine = String.format("%-5s [%s] [%s] %s%s",
				                               event.level().name(),
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
