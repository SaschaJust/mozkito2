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

import org.junit.Test;

/**
 * The Class ContractTest.
 *
 * @author Sascha Just
 */
public class ContractTest {
	
	/**
	 * Test asserts not null.
	 */
	@Test
	public final void testAssertsNotNull() {
		try {
			Asserts.notNull(null);
			fail("Ensures.notNull did not trigger an AssertionError on input (null).");
		} catch (final AssertionError ae) {
			final String message = ae.getMessage();
			assertThat("Checking pinpoint string.", message, containsString(getClass().getName()));
			assertThat("Checking assertion string.", message, containsString("ASSERTION"));
		}
	}
	
	/**
	 * Test ensures not null.
	 */
	@Test
	public final void testEnsuresNotNull() {
		try {
			Ensures.notNull(null);
			fail("Ensures.notNull did not trigger an AssertionError on input (null).");
		} catch (final AssertionError ae) {
			final String message = ae.getMessage();
			assertThat("Checking pinpoint string.", message, containsString(getClass().getName()));
			assertThat("Checking postcondition string.", message, containsString("POSTCONDITION"));
		}
	}
	
}
