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
