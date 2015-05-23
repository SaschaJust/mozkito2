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

package org.mozkito.skeleton.messages.i18n;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class MessagesTest.
 *
 * @author Sascha Just
 */
public class MessagesTest {
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Messages.setLocale(Locale.US);
	}
	
	/**
	 * Test single.
	 */
	@Test
	public final void testSingle() {
		final String m = Messages.get("test.single");
		assertThat("String must not be empty.", m, not(nullValue()));
		assertThat("String has to match.", m, equalTo("Test single."));
	}
	
	/**
	 * Test three different.
	 */
	@Test
	public final void testThreeDifferent() {
		final String m = Messages.get("test.three.different", "a", "b", "c");
		assertThat("String must not be empty.", m, not(nullValue()));
		assertThat("String has to match.", m, equalTo("Test three different (a, b, c)."));
	}
	
	/**
	 * Test two multi.
	 */
	@Test
	public final void testTwoMulti() {
		final String m = Messages.get("test.two.multi", "a", "b");
		assertThat("String must not be empty.", m, not(nullValue()));
		assertThat("String has to match.", m, equalTo("Test two multi (a, b, a, a)."));
	}
}
