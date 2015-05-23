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

package org.mozkito.core.libs.versions;

import java.math.BigInteger;

/**
 * The Class Types.
 *
 * @author Sascha Just
 */
public class Types {
	
	/**
	 * Big integer to hex40.
	 *
	 * @param hash
	 *            the hash
	 * @return the string
	 */
	public static String bigIntegerToHex(final BigInteger hash) {
		return hash.toString(16);
	}
	
	/**
	 * Hex40 to long.
	 *
	 * @param hash
	 *            the hash
	 * @return the big integer
	 */
	public static BigInteger hexToBigInteger(final String hash) {
		return new BigInteger(hash, 16);
	}
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		BigInteger i = hexToBigInteger("8e03341c559787208e06cdaeb6a4b26af6b85b48");
		System.err.println(i);
		System.err.println(bigIntegerToHex(i));
		i = hexToBigInteger("ffffffffffffffffffffffffffffffffffffffff");
		System.err.println(i);
	}
	
	/**
	 * Instantiates a new types.
	 */
	private Types() {
		// avoid instantiation
	}
}
