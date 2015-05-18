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

import org.mozkito.skeleton.contracts.internal.RequirementNotMetExeption;
import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Contract.
 *
 * @author Sascha Just
 */
public class Contract {
	
	/**
	 * Asserts.
	 *
	 * @param next
	 *            the next
	 */
	public static void asserts(final boolean next) {
		assert next : Messages.get("skeleton.contract.asserts.failed", getCallerString());
	}
	
	/**
	 * Ensures.
	 *
	 * @param next
	 *            the next
	 */
	public static void ensures(final boolean next) {
		assert next : Messages.get("skeleton.contract.ensures.failed", getCallerString());
	}
	
	/**
	 * Gets the caller string.
	 * 
	 * @return a string representing the line of code the condition was triggered from.
	 */
	static final String getCallerString() {
		final Throwable throwable = new Throwable();
		
		throwable.fillInStackTrace();
		
		final Integer lineNumber = throwable.getStackTrace()[2].getLineNumber();
		final String methodName = throwable.getStackTrace()[2].getMethodName();
		final String className = throwable.getStackTrace()[2].getClassName();
		
		return "[" + className + "::" + methodName + "#" + lineNumber + "] ";
	}
	
	/**
	 * Checks the evaluated condition and throws a {@link RequirementNotMetExeption} which derives from
	 * {@link IllegalArgumentException}.
	 *
	 * @param value
	 *            the value
	 */
	public static void requires(final boolean value) {
		if (!value) {
			throw new RequirementNotMetExeption(
			                                    Messages.get("skeleton.contract.requires.failed", getCallerString(), ""));
		}
	}
	
	/**
	 * Requires.
	 *
	 * @param b
	 *            the b
	 * @param message
	 *            the message
	 */
	public static void requires(final boolean b,
	                            final String message) {
		if (!b) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.failed", getCallerString(),
			                                                message));
		}
	}
	
	/**
	 * Requires.
	 *
	 * @param b
	 *            the b
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void requires(final boolean b,
	                            final String formatString,
	                            final Object... arguments) {
		if (!b) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.failed", getCallerString(),
			                                                String.format(formatString, arguments)));
		}
	}
	
	/**
	 * Instantiates a new contract.
	 */
	private Contract() {
		// avoid instantiation.
	}
}
