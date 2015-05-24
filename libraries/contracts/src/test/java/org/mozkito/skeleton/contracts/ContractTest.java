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
