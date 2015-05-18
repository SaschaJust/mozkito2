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
import java.util.Map;

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Requires.
 */
public class Requires {
	
	/**
	 * Contains key.
	 *
	 * @param <X>
	 *            the generic type
	 * @param <Y>
	 *            the generic type
	 * @param map
	 *            the map
	 * @param key
	 *            the key
	 */
	public static <X, Y> void containsKey(final Map<X, Y> map,
	                                      final X key) {
		if (map != null && !map.containsKey(key)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.containsKey",
			                                                Contract.getCallerString(), ""));
		}
	}
	
	/**
	 * Contains key.
	 *
	 * @param <X>
	 *            the generic type
	 * @param <Y>
	 *            the generic type
	 * @param map
	 *            the map
	 * @param key
	 *            the key
	 * @param message
	 *            the message
	 */
	public static <X, Y> void containsKey(final Map<X, Y> map,
	                                      final X key,
	                                      final String message) {
		if (map != null && !map.containsKey(key)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.containsKey",
			                                                Contract.getCallerString(), message));
		}
	}
	
	/**
	 * Contains key.
	 *
	 * @param <X>
	 *            the generic type
	 * @param <Y>
	 *            the generic type
	 * @param map
	 *            the map
	 * @param key
	 *            the key
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static <X, Y> void containsKey(final Map<X, Y> map,
	                                      final X key,
	                                      final String formatString,
	                                      final Object... arguments) {
		if (map != null && !map.containsKey(key)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.containsKey",
			                                                Contract.getCallerString(),
			                                                String.format(formatString, arguments)));
		}
	}
	
	/**
	 * Equal to.
	 *
	 * @param arg
	 *            the arg
	 * @param ref
	 *            the ref
	 */
	public static void equalTo(final Object arg,
	                           final Object ref) {
		if (ref != null) {
			if (!ref.equals(arg)) {
				throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.equalTo",
				                                                Contract.getCallerString()));
			}
		} else if (arg != null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.equalTo",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks if is integer.
	 *
	 * @param object
	 *            the object
	 */
	public static void isInteger(final Object object) {
		if (object != null && !(object instanceof Integer)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.isInteger",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks if is long.
	 *
	 * @param object
	 *            the object
	 */
	public static void isLong(final Object object) {
		if (object != null && !(object instanceof Long)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.isLong",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks if is null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void isNull(final Object arg) {
		if (arg != null) {
			throw new NullPointerException(
			                               Messages.get("skeleton.contract.requires.isNull", Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks the requirement that the argument must not be empty. Using (null) arguments is valid.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notEmpty(final Collection<?> arg) {
		if (arg != null && arg.isEmpty()) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notEmpty",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks the requirement that the argument must not be empty. Using (null) arguments is valid.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notEmpty(final Object[] arg) {
		if (arg != null && arg.length == 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notEmpty",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Not empty.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notEmpty(final String arg) {
		if (arg != null && arg.isEmpty()) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notEmpty",
			                                                Contract.getCallerString(), ""));
		}
	}
	
	/**
	 * Not empty.
	 *
	 * @param arg
	 *            the arg
	 * @param message
	 *            the message
	 */
	public static void notEmpty(final String arg,
	                            final String message) {
		if (arg != null && arg.isEmpty()) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notEmpty",
			                                                Contract.getCallerString(), message));
		}
	}
	
	/**
	 * Not equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 */
	public static void notEqualTo(final String lhs,
	                              final String rhs) {
		if (lhs != null && !lhs.equals(rhs)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(), ""));
		} else if (rhs != null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(), ""));
		}
	}
	
	/**
	 * Not equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 * @param message
	 *            the message
	 */
	public static void notEqualTo(final String lhs,
	                              final String rhs,
	                              final String message) {
		if (lhs != null && !lhs.equals(rhs)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(), message));
		} else if (rhs != null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(), message));
		}
	}
	
	/**
	 * Not equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void notEqualTo(final String lhs,
	                              final String rhs,
	                              final String formatString,
	                              final Object... arguments) {
		if (lhs != null && !lhs.equals(rhs)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(),
			                                                String.format(formatString, arguments)));
		} else if (rhs != null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString(),
			                                                String.format(formatString, arguments)));
		}
	}
	
	/**
	 * Not negative.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notNegative(final Integer arg) {
		if (arg != null && arg < 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notNegative",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks the requirement that the argument must not be null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notNull(final Object arg) {
		if (arg == null) {
			throw new NullPointerException(Messages.get("skeleton.contract.requires.notNull",
			                                            Contract.getCallerString()));
		}
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
		if (arg == null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notNull",
			                                                Contract.getCallerString(), message));
		}
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
		if (arg == null) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notNull",
			                                                Contract.getCallerString(),
			                                                String.format(formatString, arguments)));
		}
	}
	
	/**
	 * Positive.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void positive(final Integer arg) {
		if (arg != null && arg <= 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.positive",
			                                                Contract.getCallerString(), ""));
		}
	}
	
	/**
	 * Positive.
	 *
	 * @param arg
	 *            the arg
	 * @param message
	 *            the message
	 */
	public static void positive(final Integer arg,
	                            final String message) {
		if (arg != null && arg <= 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.positive",
			                                                Contract.getCallerString(), message));
		}
	}
	
	/**
	 * Positive.
	 *
	 * @param arg
	 *            the arg
	 * @param formatString
	 *            the format string
	 * @param arguments
	 *            the arguments
	 */
	public static void positive(final Integer arg,
	                            final String formatString,
	                            final Object... arguments) {
		if (arg != null && arg <= 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.positive",
			                                                Contract.getCallerString(),
			                                                String.format(formatString, arguments)));
		}
	}
	
	/**
	 * Positive.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void positive(final Long arg) {
		if (arg != null && arg <= 0) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.positive",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Valid index.
	 *
	 * @param index
	 *            the index
	 * @param array
	 *            the array
	 */
	public static void validIndex(final int index,
	                              final Object[] array) {
		if (array != null && (index < 0 || array.length <= index)) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.validIndex",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Instantiates a new requires.
	 */
	private Requires() {
		// avoid instantiation.
	}
}
