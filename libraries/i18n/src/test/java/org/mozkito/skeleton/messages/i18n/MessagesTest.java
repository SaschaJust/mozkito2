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
