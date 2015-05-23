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

package org.mozkito.skeleton.settings;

/**
 * The Class Environment.
 *
 * @author Sascha Just
 */
public class Environment {
	
	/**
	 * Returns the pre-computed value whether or not assertions are being processed by the JVM.
	 *
	 * @return true, if assertions are enabled (-ea)
	 */
	public static boolean assertionsEnabled() {
		return ASSERTIONS_ENABLED;
	}
	
	/** The assertions enabled. */
	private static boolean ASSERTIONS_ENABLED;
	
	static {
		try {
			assert false;
			ASSERTIONS_ENABLED = false;
		} catch (final AssertionError ae) {
			ASSERTIONS_ENABLED = true;
		}
	}
	
	/**
	 * Instantiates a new environment.
	 */
	private Environment() {
		// avoid instantiation
	}
}
