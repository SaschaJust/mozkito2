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

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Requires.
 */
public class Requires {
	
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
	 * Checks the requirement that the argument must not be empty. Using (null) arguments is valid.
	 *
	 * @param arg
	 *            the arg
	 * @throws IllegalArgumentException
	 *             the null pointer exception
	 */
	public static void notEmpty(final Collection<?> arg) {
		if (arg != null && arg.isEmpty()) {
			throw new IllegalArgumentException(Messages.get("skeleton.contract.requires.notEmpty",
			                                                Contract.getCallerString()));
		}
	}
	
	/**
	 * Checks the requirement that the argument must not be null.
	 *
	 * @param arg
	 *            the arg
	 * @throws NullPointerException
	 *             the null pointer exception
	 */
	public static void notNull(final Object arg) {
		if (arg == null) {
			throw new NullPointerException(Messages.get("skeleton.contract.requires.notNull",
			                                            Contract.getCallerString()));
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
			                                                Contract.getCallerString()));
		}
	}
	
	private Requires() {
		// avoid instantiation.
	}
}
