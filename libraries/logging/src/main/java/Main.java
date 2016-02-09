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

import org.mozkito.libraries.logging.Bus;
import org.mozkito.libraries.logging.Level;
import org.mozkito.libraries.logging.Line;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.logging.consumer.LogConsumer;
import org.mozkito.libraries.logging.consumer.appender.TerminalAppender;

/**
 * The Class Main.
 *
 * @author Sascha Just
 */
public class Main {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		new LogConsumer(Bus.provider).register(new TerminalAppender(Level.INFO));
		
		Logger.error.append('a').append('b').append('\n').append('c').append('\n').append('c');
		Logger.warn.append("test\nthis\nshit\nasd");
		Logger.info.println("test");
		Logger.debug.println("again");
		final Exception e = new RuntimeException();
		e.fillInStackTrace();
		e.printStackTrace(Logger.trace);
		Bus.notify(Level.FATAL, Line.LOGS, new NullPointerException(), "This is a %s.", "yeeahaa");
	}
}
