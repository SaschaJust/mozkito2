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

import org.mozkito.skeleton.i18n.Messages;

/**
 * The Class Assert.
 */
public class Asserts {
	
	/**
	 * Equal to.
	 *
	 * @param lhs
	 *            the lhs
	 * @param rhs
	 *            the rhs
	 */
	public static void equalTo(final int lhs,
	                           final int rhs) {
		assert lhs == rhs : Messages.get("skeleton.contract.asserts.equalTo", Contract.getCallerString());
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
	 * @param object
	 */
	public static void isInteger(final Object object) {
		assert object == null || object instanceof Integer : Messages.get("skeleton.contract.asserts.isInteger",
		                                                                  Contract.getCallerString());
	}
	
	/**
	 * Not null.
	 *
	 * @param arg
	 *            the arg
	 */
	public static void notNull(final Object arg) {
		assert arg != null : Contract.getCallerString() + "Assertion violated: Object must not be (null).";
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
		assert ids == null || i < ids.length : Messages.get("skeleton.contract.asserts.validIndex",
		                                                    Contract.getCallerString());
	}
	
	private Asserts() {
		// avoid instantiation.
	}
}