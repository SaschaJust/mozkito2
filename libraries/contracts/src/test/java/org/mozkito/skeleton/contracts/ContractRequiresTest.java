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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class ContractTest.
 *
 * @author Sascha Just
 */
public class ContractRequiresTest {
	
	/** The Constant PRECONDITION_TAG. */
	private static final String PRECONDITION_TAG = "PRECONDITION VIOLATED";
	
	/**
	 * Sets the locale to US for string comparison.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		Messages.setLocale(Locale.US);
	}
	
	/**
	 * Test requires.
	 */
	@Test
	public final void testRequires() {
		try {
			Contract.requires(false);
			fail("Contract.requires did not trigger a RequirementNotMetException on input false.");
		} catch (final IllegalArgumentException e) {
			final String message = e.getMessage();
			assertThat("Checking pinpoint string.", message, containsString(getClass().getName()));
			assertThat("Checking precondition string.", message, containsString(PRECONDITION_TAG));
		}
	}
	
	/**
	 * Test requires not empty.
	 */
	@Test
	public final void testRequiresNotEmpty() {
		try {
			Requires.notEmpty((Object[]) null);
		} catch (final Throwable t) {
			fail("Empty check should pass on (null) argument.");
		}
		
		try {
			Requires.notEmpty(new LinkedList<String>());
			fail("Requires.notNull did not trigger an IllegalArgumentException on input empty list.");
		} catch (final IllegalArgumentException e) {
			final String message = e.getMessage();
			assertThat("Checking pinpoint string.", message, containsString(getClass().getName()));
			assertThat("Checking precondition string.", message, containsString(PRECONDITION_TAG));
		}
	}
	
	/**
	 * Test requires not null.
	 */
	@Test
	public final void testRequiresNotNull() {
		try {
			Requires.notNull(null);
			fail("Requires.notNull did not trigger a NullPointerException on input (null).");
		} catch (final NullPointerException e) {
			final String message = e.getMessage();
			assertThat("Checking pinpoint string.", message, containsString(getClass().getName()));
			assertThat("Checking precondition string.", message, containsString(PRECONDITION_TAG));
		}
	}
}
