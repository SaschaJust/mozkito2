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

package org.mozkito.libraries.logging.matchers;

import org.mozkito.libraries.logging.Level;

/**
 * The Interface IMatcher.
 *
 * @author Sascha Just
 */
public interface IMatcher {
	
	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	Level getMax();
	
	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	Level getMin();
	
	/**
	 * Matches.
	 *
	 * @param message
	 *            the message
	 * @param level
	 *            the level
	 * @param threadName
	 *            the thread name
	 * @return true, if successful
	 */
	boolean matches(String message,
	                Level level,
	                String threadName);
}
