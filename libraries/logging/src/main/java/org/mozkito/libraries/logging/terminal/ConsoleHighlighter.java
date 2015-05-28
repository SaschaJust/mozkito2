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
package org.mozkito.libraries.logging.terminal;

/**
 * The Class Highlighter.
 *
 * @author Sascha Just
 */
public abstract class ConsoleHighlighter implements IHighlighter {
	
	/** The color format. */
	private String colorFormat = null;
	
	/**
	 * Colors.
	 *
	 * @return the terminal color[]
	 */
	public abstract TerminalColor[] colors();
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.logging.terminal.IHighlighter#highlight(java.lang.String)
	 */
	@Override
	public final String highlight(final String message) {
		if (this.colorFormat == null) {
			synchronized (this) {
				if (this.colorFormat == null) {
					final StringBuilder builder = new StringBuilder();
					for (final TerminalColor color : colors()) {
						builder.append(color.getTag());
					}
					this.colorFormat = builder.toString();
				}
			}
		}
		
		return this.colorFormat + message + TerminalColor.NONE.getTag();
	}
	
}
