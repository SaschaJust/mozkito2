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
	 * @param value
	 *            the next
	 */
	public static void asserts(final boolean value) {
		assert value : Messages.get("skeleton.contracts.asserts.failed", getCallerString(), "");
	}
	
	/**
	 * Asserts.
	 *
	 * @param value
	 *            the value
	 * @param message
	 *            the message
	 */
	public static void asserts(final boolean value,
	                           final String message) {
		assert value : Messages.get("skeleton.contracts.asserts.failed", getCallerString(), message);
	}
	
	/**
	 * Asserts.
	 *
	 * @param value
	 *            the value
	 * @param formatString
	 *            the format string
	 * @param args
	 *            the args
	 */
	public static void asserts(final boolean value,
	                           final String formatString,
	                           final Object... args) {
		assert value : Messages.get("skeleton.contracts.asserts.failed", getCallerString(),
		                            String.format(formatString, args));
	}
	
	/**
	 * Ensures.
	 *
	 * @param value
	 *            the next
	 */
	public static void ensures(final boolean value) {
		assert value : Messages.get("skeleton.contracts.ensures.failed", getCallerString(), "");
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
		final String fileName = throwable.getStackTrace()[2].getFileName();
		
		return "[" + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")] ";
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
			throw new RequirementNotMetExeption(Messages.get("skeleton.contracts.requires.failed", getCallerString(),
			                                                 ""));
		}
	}
	
	/**
	 * Requires.
	 *
	 * @param value
	 *            the b
	 * @param message
	 *            the message
	 */
	public static void requires(final boolean value,
	                            final String message) {
		if (!value) {
			throw new RequirementNotMetExeption(Messages.get("skeleton.contracts.requires.failed", getCallerString(),
			                                                 message));
		}
	}
	
	/**
	 * Requires.
	 *
	 * @param value
	 *            the b
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void requires(final boolean value,
	                            final String formatString,
	                            final Object... arguments) {
		if (!value) {
			throw new RequirementNotMetExeption(Messages.get("skeleton.contracts.requires.failed", getCallerString(),
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
