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

package org.mozkito.skeleton.contracts;

import java.util.Collection;

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Ensures.
 */
public class Ensures {
	
	/**
	 * Empty.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void empty(final Collection<?> arg) {
		assert arg == null || arg.isEmpty();
	}
	
	/**
	 * Not null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notNull(final Object arg) {
		assert arg != null : Messages.get("skeleton.contracts.ensures.notNull", Contract.getCallerString(), "");
	}
	
	/**
	 * Not null.
	 *
	 * @param arg
	 *            the arg
	 * @param message
	 *            the message
	 */
	public static void notNull(final Object arg,
	                           final String message) {
		assert arg != null : Messages.get("skeleton.contracts.ensures.notNull", Contract.getCallerString(), message);
	}
	
	/**
	 * Not null.
	 *
	 * @param arg
	 *            the arg
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void notNull(final Object arg,
	                           final String formatString,
	                           final Object... arguments) {
		assert arg != null : Messages.get("skeleton.contracts.ensures.notNull", String.format(formatString, arguments));
	}
	
	/**
	 * Instantiates a new ensures.
	 */
	private Ensures() {
		// avoid instantiation.
	}
}
