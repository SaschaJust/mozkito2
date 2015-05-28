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

import java.util.Iterator;
import java.util.Map;

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Assert.
 */
public class Asserts {
	
	/**
	 * Contains.
	 *
	 * @param <T>
	 *            the generic type
	 * @param map
	 *            the map
	 * @param key
	 *            the key
	 */
	public static <T> void containsKey(final Map<T, ?> map,
	                                   final T key) {
		assert map == null || map.containsKey(key) : Messages.get("skeleton.contracts.asserts.containsKey",
		                                                          Contract.getCallerString(), "");
	}
	
	/**
	 * Asserts that given map (if not <code>null</code>) contains the given key.
	 * 
	 * The example below will raise an AssertionError, if assertions are enabled (VMARG -ea).
	 * 
	 * <pre>
	 * Map&lt;String, String&gt; friends = new HashMap&lt;&gt;();
	 * map.put(&quot;Peter&quot;, &quot;Maggie&quot;);
	 * map.put(&quot;Peter&quot;, &quot;Emma&quot;);
	 * map.put(&quot;Emma&quot;, &quot;Maggie&quot;);
	 * 
	 * Asserts.containsKey(&quot;Maggie&quot;);
	 * </pre>
	 *
	 * @param <T>
	 *            the generic type of the key.
	 * @param map
	 *            the map which needs to contain the key
	 * @param key
	 *            the key to check for.
	 * @param formatString
	 *            the format string of the error raised, in case the assertion triggers.
	 * @param arguments
	 *            the arguments to the format string.
	 */
	public static <T> void containsKey(final Map<T, ?> map,
	                                   final T key,
	                                   final String formatString,
	                                   final Object... arguments) {
		assert map == null || map.containsKey(key) : Messages.get("skeleton.contracts.asserts.containsKey",
		                                                          Contract.getCallerString(),
		                                                          String.format(formatString, arguments));
	}
	
	/**
	 * Equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 */
	public static void equalTo(final Object lhs,
	                           final Object rhs) {
		assert lhs == null || lhs.equals(rhs) : Messages.get("skeleton.contracts.asserts.equalTo",
		                                                     Contract.getCallerString(), "");
	}
	
	/**
	 * Equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 * @param formatString
	 *            the format string
	 * @param args
	 *            the args
	 */
	public static void equalTo(final Object lhs,
	                           final Object rhs,
	                           final String formatString,
	                           final Object... args) {
		assert lhs == null || lhs.equals(rhs) : Messages.get("skeleton.contracts.asserts.equalTo",
		                                                     Contract.getCallerString(),
		                                                     String.format(formatString, args));
	}
	
	/**
	 * Greater.
	 *
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param message
	 *            the message
	 */
	public static void greater(final int left,
	                           final int right,
	                           final String message) {
		assert left > right : Contract.getCallerString() + "Assertion violated: LHS has to be greater than RHS.";
	}
	
	/**
	 * Checks for next.
	 *
	 * @param it
	 *            the it
	 */
	public static void hasNext(final Iterator<?> it) {
		assert it == null || it.hasNext() : Messages.get("skeleton.contracts.asserts.hasNext",
		                                                 Contract.getCallerString(), "");
	}
	
	/**
	 * Checks for next.
	 *
	 * @param it
	 *            the it
	 * @param message
	 *            the message
	 */
	public static void hasNext(final Iterator<?> it,
	                           final String message) {
		assert it == null || it.hasNext() : Messages.get("skeleton.contracts.asserts.hasNext",
		                                                 Contract.getCallerString(), message);
	}
	
	/**
	 * Checks if is integer.
	 *
	 * @param object
	 *            the object
	 */
	public static void isInteger(final Object object) {
		assert object == null || object instanceof Integer : Messages.get("skeleton.contracts.asserts.isInteger",
		                                                                  Contract.getCallerString());
	}
	
	/**
	 * Checks if is null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void isNull(final Object arg) {
		assert arg == null : Messages.get("skeleton.contracts.asserts.isNull", Contract.getCallerString(), "");
	}
	
	/**
	 * Checks if is null.
	 *
	 * @param arg
	 *            the arg
	 * @param message
	 *            the message
	 */
	public static void isNull(final Object arg,
	                          final String message) {
		assert arg == null : Messages.get("skeleton.contracts.asserts.isNull", Contract.getCallerString(), message);
	}
	
	/**
	 * Checks if is null.
	 *
	 * @param arg
	 *            the arg
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void isNull(final Object arg,
	                          final String formatString,
	                          final Object... arguments) {
		assert arg == null : Messages.get("skeleton.contracts.asserts.isNull", Contract.getCallerString(),
		                                  String.format(formatString, arguments));
	}
	
	/**
	 * Less.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 * @param message
	 *            the message
	 */
	public static void less(final Integer lhs,
	                        final Integer rhs,
	                        final String message) {
		assert lhs < rhs : Messages.get("skeleton.contracts.asserts.less", Contract.getCallerString());
	}
	
	/**
	 * Not negative.
	 *
	 * @param arg
	 *            the arg
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void notNegative(final Integer arg,
	                               final String formatString,
	                               final Object... arguments) {
		assert arg >= 0 : Messages.get("skeleton.contracts.asserts.notNegative", Contract.getCallerString(),
		                               String.format(formatString, arguments));
	}
	
	/**
	 * Not null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notNull(final Object arg) {
		assert arg != null : Messages.get("skeleton.contracts.asserts.notNull", Contract.getCallerString(), "");
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
		assert arg != null : Messages.get("skeleton.contracts.asserts.notNull", Contract.getCallerString(), message);
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
		assert arg != null : Messages.get("skeleton.contracts.asserts.notNull", Contract.getCallerString(),
		                                  String.format(formatString, arguments));
	}
	
	/**
	 * @param id
	 */
	public static void positive(final Integer id) {
		assert id == null || id > 0 : Messages.get("skeleton.contracts.asserts.positive", Contract.getCallerString(),
		                                           "");
	}
	
	/**
	 * Positive.
	 *
	 * @param id
	 *            the id
	 */
	public static void positive(final Long id) {
		assert id == null || id > 0 : Messages.get("skeleton.contracts.asserts.positive", Contract.getCallerString(),
		                                           "");
	}
	
	/**
	 * Valid index.
	 *
	 * @param i
	 *            the i
	 * @param ids
	 *            the ids
	 */
	public static void validIndex(final int i,
	                              final Object[] ids) {
		assert ids == null || i < ids.length : Messages.get("skeleton.contracts.asserts.validIndex",
		                                                    Contract.getCallerString(), "");
	}
	
	/**
	 * Instantiates a new asserts.
	 */
	private Asserts() {
		// avoid instantiation.
	}
}
