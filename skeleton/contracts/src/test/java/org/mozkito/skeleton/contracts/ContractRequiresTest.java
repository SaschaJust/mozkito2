/***********************************************************************************************************************
 * Copyright 2014 Sascha Just
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
